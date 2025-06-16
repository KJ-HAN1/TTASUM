import logging
import os
import re

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
	""" 조직도 데이터를 미리 준비하여 LLM 체인을 생성합니다.

	RDB에서 데이터를 로드하여 개별 Document로 변환하고, 이를 작은 청크로 분할합니다.
	분할된 청크는 벡터 DB에 임베딩하여 저장되며, 이 데이터를 기반으로 LLM 프롬프트와 연결된
	전체 LLM 체인이 생성됩니다.

	Args:
		app: FastAPI 인스턴스.

	Raises:
		DBConnectionError: 데이터베이스 연결에 실패하거나, 쿼리 실행 중 MySQL 오류가 발생할 경우.
		GeneralError: MySQL 관련 오류 외의 예상치 못한 일반적인 오류가 발생할 경우.
					  (예: 환경 변수 설정 오류 등)

	"""

	# .env 파일을 읽어 환경 변수로 로드
	load_dotenv()

	data = await connect_database()
	splits = await split_into_chunks(app, data)
	await embed_and_store(app, splits)
	await create_chain(app)


async def create_chain(app):
	""" 언어 모델을 생성하고 프롬프트와 결합하여 LLM 체인을 생성합니다.

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
	prompt_template = PromptTemplate.from_template(
		"""
		   ## 시스템 메시지
		   - 제공된 지침을 **반드시 준수**하여 [사용자 질문]에 답변합니다.   
		   
		   ## 역할 및 지침
		   - 당신은 한국장기조직기증원(KODA)의 조직 안내 챗봇으로서 [사용자 질문]과 관련된 부서를 [컨텍스트]에서 찾고, 
		   해당 부서의 업무와 전화번호를 안내합니다.
		   - 사용자의 질문에 항상 **정중하고 예의있는 어투**로 응대합니다.
		   - [답변 생성 규칙]의 조건문('~라면', '~일/ㄹ 경우')에 해당할 때만 규칙에 따라 지정된 형식으로 응답합니다.
		   - 본부명, 1차/2차 하위부서/직급, 전화번호는 **100% 정확하게** 답변합니다.
		   - 답변은 최대 150~200자 내외로 유지합니다.
		   
		   ## 답변 생성 규칙
		   - ** 아래 규칙은 사용자의 질문에 답변을 생성하기 위한 내부 정보이니, 최종 답변에 직접 노출하지 마세요. **
		   - ** 아래 규칙들은 [사용자 질문]을 받은 즉시, 명시된 순서대로 엄격하게 평가하고, 오직 조건에 해당하는 첫 번째 
		   규칙의 응답만 생성합니다. 이때 다른 모든 지침 및 컨텍스트 내용은 무시합니다. **
		   - ** 조건문에 해당하는지를 [판단 과정]을 통해 단계적으로 생각합니다. **
		   
		   1. [사용자 질문]이 '안녕하세요', '감사합니다' 등과 유사한 어조와 형태를 띠는 인사말이라면 **[컨텍스트]를 
		   전혀 참고하지 않고** 짧은 인사말로 응대합니다.
				- 예시: 안녕, 안녕하세요, 안녕하세요!, 반갑습니다, 감사합니다, 감사합니다!!!, 땡큐
		   
		   2. [사용자 질문]이 장기 기증에 대한 부정적인 말이나 비속어라면 동조하지 않고 다시 질문해 달라고 요청합니다. 
		   3. [컨텍스트] 내 '본부명' 키 값이 '권역별 지부'인 내용이 명시적으로 포함되어 있고, 동시에 [사용자 질문]에 
		   특정 지역, 권역(중부, 충청호남, 영남 지부)이 명시되지 않았다면  **[컨텍스트] 내 다른 모든 상세 정보(부서명, 
		   담당 업무, 전화번호 등)를 무시하고** 오직 다음 문장으로만 응대합니다: 
			   "안녕하세요! 현재 문의 주신 내용은 권역별로 독립적으로 담당하고 있습니다. 어느 지부로 문의하시는지 알려주시면 
			   더욱 정확한 안내를 드릴 수 있습니다. (ex. 중부, 충청호남, 영남)"
			   - 예시
                [사용자 질문] 잠재장기기증자 기증동의 문의
			   [컨텍스트] 본부명: 권역별 지부, 1차 하위부서/직급: 충청호남 지부, 2차 하위부서/직급: 지부장, 
			   담당 업무: 지부 업무 총괄
			   [판단 과정] **본부명**이 '권역별 지부'이므로 조건문에 해당함 (1/2), 사용자 질문에 지역, 
			   권역 내용이 포함되지 않았으니 조건문에 완전 충족함(2/2)
			   [최종 응답] 안녕하세요! 현재 문의 주신 내용은 권역별로 독립적으로 담당하고 있습니다. 어느 지부로 
			   문의하시는지 알려주시면 더욱 정확한 안내를 드릴 수 있습니다. (ex. 중부, 충청호남, 영남)
			   
		   4. [사용자 질문]이 한국장기조직기증원(KODA)의 업무(경영관리, 기증관리 등)와 관련이 없을 경우, 다음과 같이 
		   완곡하게 응대합니다:
			   "안녕하세요! 한국장기조직기증원(KODA)입니다. 문의해 주셔서 감사합니다. 현재 문의 주신 내용은 저희 챗봇이 
			   답변할 수 있는 분야와 다소 거리가 있어 자세한 안내가 어렵습니다. 장기 기증과 관련된 일반적인 문의가 
			   있으시다면, KODA 홈페이지를 방문하시거나, 대표 전화번호(02-3447-5632) 또는 채널톡, 
			   카카오톡/인스타그램을 통해 문의해 주시면 친절하게 안내해 드리겠습니다. 감사합니다."
			   * 이 답변은 [사용자 질문]이 컨텍스트와 무관한 질문일 때만 사용하며, 사용자에게 '관련 없다'는 식의 부정적 
			   어조를 피하고 다른 상담 채널로의 접근을 적극적으로 제공합니다. *
			   - 예시
			   [사용자 질문] 오늘 날씨가 어떤가요?, 오늘은 며칠인가요?, 조직 위치
			   [판단 과정] 날씨, 날짜 등은 한국장기조직기증원의 업무(경영관리, 기증관리 등)와 관련이 없으므로 조건문에 
			   해당함 (1/1)
			   [최종 응답] 안녕하세요! 한국장기조직기증원(KODA)입니다. 문의해 주셔서 감사합니다. 현재 문의 주신 내용은 
			   저희 챗봇이 답변할 수 있는 분야와 다소 거리가 있어 자세한 안내가 어렵습니다. 장기 기증과 관련된 일반적인 
			   문의가 있으시다면, KODA 홈페이지를 방문하시거나, 대표 전화번호(02-3447-5632) 또는 채널톡, 
			   카카오톡/인스타그램을 통해 문의해 주시면 친절하게 안내해 드리겠습니다. 감사합니다.
		   
		   ## 사용자 질문:
		   {question}
		   
		   ## 컨텍스트:
		   {context}
		   
		   안녕하세요, 한국장기조직기증원(KODA)입니다.
		""")

	# 체인 생성
	chain = prompt_template | llm
	app.state.chain = chain


async def embed_and_store(app, splits):
	""" 분할한 청크를 벡터 DB에 임베딩하여 저장합니다.

	Args:
		app: FastAPI 인스턴스.
		splits(list): 더 작은 청크로 분할한 데이터.

	"""
	# 데이터 임베딩 설정
	embeddings = UpstageEmbeddings(
		api_key=os.getenv('UPSTAGE_API_KEY'),
		model=os.getenv('EMBEDDING_MODEL_NAME')
	)
	vector_store = Chroma(embedding_function=embeddings)

	# 벡터 DB에 저장
	logger.info('[DB EMBEDDING] 벡터 DB에 데이터 임베딩 및 저장 중 ...')
	vector_store.add_documents(splits)
	logger.info('[DB LOAD] 벡터 DB에 데이터 저장 완료')
	app.state.retriever = vector_store.as_retriever(
		search_type="similarity",
		search_kwargs={"k": 1}
	)


async def split_into_chunks(app, data):
	""" DB에서 불러온 데이터를 Document로 변환하고 더 작은 청크로 분할합니다.

	Args:
		app: FastAPI 인스턴스.
		data(list): DB 테이블에서 전체 조회한 데이터.

	Returns:
		splits(list): 더 작은 청크로 분할한 데이터.

	"""
	# DB 레코드를 개별적인 Document로 생성
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
	app.state.docs = docs

	# 더 작은 청크로 분할
	splits = []
	for doc in docs:
		for chunk in re.split(r'[\r\n\t·,]+', doc.page_content):
			splits.append(Document(
				metadata=doc.metadata,
				page_content=chunk
			))
	return splits


async def connect_database():
	""" MySQL 데이터베이스를 연결하고 데이터를 로드합니다.

	Returns:
		data(list): DB 테이블에서 전체 조회한 데이터

	Raises:
		DBConnectionError: 데이터베이스 연결에 실패하거나, 쿼리 실행 중 MySQL 오류가 발생할 경우.
		GeneralError: MySQL 관련 오류 외의 예상치 못한 일반적인 오류가 발생할 경우.
					  (예: 환경 변수 설정 오류 등)

	"""
	conn = None
	chart_table = os.getenv('TBL_ORG_CHART')
	stmt = f"SELECT * FROM {chart_table}"  # 테이블 데이터 전체 조회
	try:
		# MySQL 데이터베이스 연결 및 예외 처리
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
			logger.info('[DB DISCONNECT] 데이터베이스 연결 닫기 성공')
			conn.close()
	return data
