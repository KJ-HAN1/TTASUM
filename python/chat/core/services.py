import logging
import os

import pymysql
from dotenv import load_dotenv
from fastapi import FastAPI
from langchain_chroma import Chroma
from langchain_core.documents import Document
from langchain_core.prompts import PromptTemplate
from langchain_upstage import UpstageEmbeddings, ChatUpstage
from pymysql.cursors import DictCursor

from .exceptions import DBConnectionError, GeneralError

logger = logging.getLogger(__name__)
httpx_logger = logging.getLogger("httpx")
httpx_logger.setLevel(logging.WARNING)  # 임베딩 로깅 감추기


async def prepare_chat_data(app: FastAPI):
	""" 응답에 참고할 데이터를 벡터 DB에 적재하고 LLM 체인을 생성합니다.

	DB에서 데이터를 로드하여 개별 Document로 변환하고, 변환된 데이터를 벡터 DB에 임베딩하여 저장합니다.
	사용자 입력을 요약하고 유형을 반환하는 프롬프트와 최종 응답을 생성하는 프롬프트를 각각 LLM과 결합하여 체인을 생성합니다.

	Args:
		app: FastAPI 인스턴스.

	Raises:
		DBConnectionError: DB 연결에 실패하거나 쿼리 실행 중 MySQL 오류가 발생할 경우.
		GeneralError: MySQL 관련 오류 외의 예상치 못한 일반적인 오류가 발생할 경우.
					  (예: 환경 변수 설정 오류 등)

	"""

	# .env 파일을 읽어 환경 변수로 로드
	load_dotenv()

	data = await connect_database()
	docs = await create_documents(app, data)
	await embed_and_store(app, docs)
	await create_chain(app)


async def create_chain(app):
	""" LLM과 프롬프트를 결합하여 LLM 체인을 생성합니다.

	Args:
		 app: FastAPI 인스턴스.

	"""
	# 언어 모델 생성
	llm = ChatUpstage(
		api_key=os.getenv('UPSTAGE_API_KEY'),
		model=os.getenv('LANG_MODEL_NAME'),
		temperature=0
	)

	# 프롬프트 템플릿 설정
	# 문어체의 데이터와 구어적인 질문 사이의 유사성 연결
	summarize_template = PromptTemplate.from_template(
		"""
		다음 사용자 질문의 핵심 의도를 파악하여, 한국장기조직기증원(KODA)의 부서 및 업무 관련 검색에 최적화된 형태로 30자 
		내외로 요약하거나 핵심 키워드를 추출해 주세요.
		
		## 판단 기준:
		- 질문이 단순 인사말인가? (예: 안녕하세요, 반갑습니다, 감사합니다 등)
		- 질문에 비속어나 부정적인 표현이 포함되어 있는가? (예: 시체, 병신, 지랄, 새끼, 미친, 또라이, 시체팔이 등)
		- 질문이 조직의 업무(경영관리, 기증관리, 유가족 지원 등)과 관련이 없는가? (예: 날씨, 날짜, 메뉴 추천 등)
			** 참고: 감사팀과 진단검사의학과 의원 업무는 '업무 무관'으로 분류하세요 ** 
		
		## 출력 형식 - 다음을 키로 하는 JSON 형식을 반환하세요. ** 단, 출력은 순수 JSON 텍스트만 반환합니다.**
		summary: [질문 요약 내용]
		type: [인사말_시작 | 인사말_종료 | 부정어/비속어 | 업무 무관 | 일반]
		---
		예시 1) 사용자: 장기 기증 신청하고 싶은데 어디에 문의하면 될까?
		요약: 장기 기증 희망 등록
		유형: 일반
		
		예시 2) 사용자: 나와 같은 장기 기증 유가족들끼리 만나서 이야기를 나눌 수 있는 공간이 있어?
		요약: 유가족 자조 모임 문의
		유형: 일반
		
		예시 3) 사용자: 안녕하세요!
		요약: 인사말
		유형: 인사말_시작
		
		예시 4) 사용자: 감사합니다
		요약: 인사말
		유형: 인사말_종료
		
		예시 5) 사용자: 병신아
		요약: 비속어 사용 감지
		유형: 부정어/비속어
		
		예시 6) 사용자: 오늘 날씨 어때?
		요약: 날씨 정보 요청
		유형: 업무 무관
		
		사용자 질문: 
		{question}
		"""
	)

	summarize_chain = summarize_template | llm
	app.state.summarize_chain = summarize_chain

	prompt_template = PromptTemplate.from_template(
		"""
		## 역할 및 지침
		- 당신은 한국장기조직기증원(KODA) 안내 챗봇으로서 [사용자 질문]과 관련된 부서를 [컨텍스트]에서 찾고, 
		해당 부서의 이름, 전화번호, 담당업무를 안내합니다.
		- 사용자의 질문을 **항상 정중하고 예의있게** 응대합니다. 
		- [답변 생성 규칙]의 조건문(~다면, '~일/ㄹ 경우')에 해당할 때만 규칙에 따라 지정된 형식으로 응답합니다.
		- 권역 및 지역은 아래 [권역 및 지역 설명]을 ** 반드시 참고합니다.**
		- 본부명, 1차/2차 하위부서/직급, 전화번호는 **100% 정확하게** 답변합니다.
		- 답변은 최대 150~300자 내외로 유지합니다.
		
		## 답변 생성 규칙
		- ** 아래 규칙은 사용자의 질문에 답변을 생성하기 위한 내부 정보이니, 최종 답변에 직접 노출하지 마세요. **
		- ** 조건문에 해당하는지를 단계적으로 생각합니다. **
		
		   1. [컨텍스트] 내 "본부명: 권역별 지부"인 내용이 포함되어 있지 않다면, [컨텍스트]에서 가장 관련도 높은 
		   부서 하나의 정보만 응답합니다.
		   
		   2. [컨텍스트] 내 "본부명: 권역별 지부"인 내용이 명시적으로 포함되어 있고, 동시에 [사용자 질문]에 특정 권역 
		   및 지역이 
		   명시되어 있지 않을 경우, 다음 내용을 따릅니다:
		   - 컨텍스트 내용을 모두 참고하여 ** 세 개의 단락으로 구성합니다. **
		   - 업무 소개는 처음 한 번만 간략히 설명하고, 각 지부 설명은 ** 관할 지역을 반드시 생략한 채 ** 전화번호를 
		   응답합니다. 
		   - 지부 순서는 중부, 충청·호남, 영남 지부 순서를 따릅니다.
		    
		   ** 단, 특정 권역 및 지역이 명시되어 있다면 가장 관련도 높은 부서 하나의 정보만 응답합니다. 이때 부서는 아래 
		   제시한 [권역 및 지역 설명]을 반드시 참고하여 해당되는 지부의 부서를 소개할 수 있도록 합니다. **
						   
		   3.[사용자 질문]이 제공된 컨텍스트와 **전혀 무관하다면** 다음과 같이 응답합니다:
			"안녕하세요! 한국장기조직기증원(KODA)입니다. 문의해 주셔서 감사합니다. 현재 문의 주신 내용은 저희 
			챗봇이 답변할 수 있는 분야와 다소 거리가 있어 자세한 안내가 어렵습니다. 장기 기증과 관련된 
			일반적인 문의가 있으시다면, KODA 홈페이지를 방문하시거나, 대표 전화번호(02-3447-5632) 또는 
			채널톡, 카카오톡/인스타그램을 통해 문의해 주시면 친절하게 안내해 드리겠습니다. 감사합니다."
		
		## 권역 및 지역 설명
		- 중부 지부: 서울, 경기, 인천, 강원, 제주
		- 충청·호남 지부: 대전, 세종, 충북, 충남, 전북, 전남, 광주
		- 영남 지부: 부산, 대구, 울산, 경북, 경남
		
		## 사용자 질문:
		{question}
		
		## 질문 유형:
		{question_type}
		
		## 컨텍스트:
		{context}
		
		안녕하세요, 한국장기조직기증원(KODA)입니다.
		"""
	)

	# 체인 생성
	chain = prompt_template | llm
	app.state.chain = chain


async def embed_and_store(app, documents):
	""" Document 객체를 벡터 DB에 임베딩하여 저장합니다.

	Args:
		app: FastAPI 인스턴스.
		documents(list): Document 객체 리스트.

	"""
	# 데이터 임베딩 설정
	embeddings = UpstageEmbeddings(
		api_key=os.getenv('UPSTAGE_API_KEY'),
		model=os.getenv('EMBEDDING_MODEL_NAME')
	)
	vector_store = Chroma(embedding_function=embeddings)

	# 벡터 DB에 저장
	logger.info('[DB EMBEDDING] 벡터 DB에 데이터 임베딩 및 저장 중 ...')
	vector_store.add_documents(documents)
	logger.info('[DB LOAD] 벡터 DB에 데이터 저장 완료')
	app.state.retriever = vector_store.as_retriever(
		search_type="similarity",
		search_kwargs={"k": 3}
	)


async def create_documents(app, data):
	""" DB에서 가져온 데이터를 Document로 변환합니다.

	Args:
		app: FastAPI 인스턴스.
		data(list): DB 테이블에서 전체 조회한 데이터.

	Returns:
		list[Document]: 개별 레코드를 Document로 변환한 객체 리스트.

	"""
	docs = []
	for idx, row in enumerate(data):
		hq_name = row['hq_name']
		dept1_name = row['dept1_name']
		dept2_name = row['dept2_name']
		job_desc = row['job_desc']
		tel_no = row['tel_no']

		docs.append(Document(
			metadata={'doc_id': idx},
			page_content=f"본부명: {hq_name}, 1차 하위부서/직급: {dept1_name}, "
						 f"2차 하위부서/직급: {dept2_name}, 담당 업무: {job_desc}, "
						 f"전화번호: {tel_no}"
		))
	return docs


async def connect_database():
	""" MySQL DB를 연결하고 데이터를 로드합니다.

	Returns:
		list: DB 테이블에서 전체 조회한 데이터.

	Raises:
		DBConnectionError: DB 연결에 실패하거나, 쿼리 실행 중 MySQL 오류가 발생할 경우.
		GeneralError: MySQL 관련 오류 외의 예상치 못한 일반적인 오류가 발생할 경우.
					  (예: 환경 변수 설정 오류 등)

	"""
	conn = None
	chart_table = os.getenv('TBL_ORG_CHART')
	stmt = f"SELECT * FROM {chart_table}"  # 테이블 데이터 전체 조회
	try:
		# MySQL DB 연결 및 예외 처리
		conn = pymysql.connect(
			host=os.getenv('DB_HOST'),
			user=os.getenv('DB_USER'),
			password=os.getenv('DB_PASSWORD'),
			database=os.getenv('DB_DATABASE'),
			cursorclass=DictCursor
		)

		with conn.cursor() as cursor:
			cursor.execute(stmt)
			data = cursor.fetchall()
			if data:
				logger.info('[DB LOAD] 데이터 로드 완료')

	except pymysql.Error as e:
		raise DBConnectionError(
			details={'table': chart_table,
					 'attempted query': stmt,
					 'error': str(e)})  # 오류 코드와 메시지 표시
	except Exception:
		raise GeneralError()
	finally:
		if conn:
			logger.info('[DB DISCONNECT] DB 연결 닫기 성공')
			conn.close()
	return data
