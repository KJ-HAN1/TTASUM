import os
from dotenv import load_dotenv
from pymysql.cursors import DictCursor
from fastapi import APIRouter, HTTPException
import pymysql
from langchain_core.documents import Document
import re
from langchain_upstage import UpstageEmbeddings
from langchain_chroma import Chroma
from langchain_upstage import ChatUpstage
from langchain_core.prompts import PromptTemplate
from models import ChatInput

# 라우터 생성
router = APIRouter(
    prefix="/chat",
    tags=['chat']
)

# .env 파일을 읽어 환경 변수로 로드
load_dotenv()

# MySQL 데이터베이스 연결 및 예외 처리
conn = pymysql.connect(
    host=os.getenv('DB_HOST'),
    user=os.getenv('DB_USER'),
    password=os.getenv('DB_PASSWORD'),
    database=os.getenv('DB_DATABASE'),
    cursorclass=DictCursor
)

chart_table=os.getenv('TBL_ORG_CHART')
stmt=f"SELECT * FROM {chart_table}"

try:
    with conn.cursor() as cursor:
        cursor.execute(stmt)
        data = cursor.fetchall()
except pymysql.Error as e:
    raise HTTPException(status_code=500, detail="서버 DB 연결에 일시적인 오류가 발생했습니다.")
finally:
    conn.close()

docs = []
for idx, row in enumerate(data):
    hq_name = row['hq_name']
    dept1_name = row['dept1_name']
    dept2_name = row['dept2_name']
    job_desc = row['job_desc']
    tel_no = row['tel_no']

    docs.append(Document(
        metadata={'doc_id': idx},
        page_content=f"본부명: {hq_name}, 1차 하위부서/직급: {dept1_name}, 2차 하위부서/직급: {dept2_name}, 담당 업무: {job_desc}, 전화번호: {tel_no}"
    ))

# text_splitter 없이 문장을 적절히 분할
splits = []
for doc in docs:
    for chunk in re.split(r'[\r\n\t·,]+', doc.page_content):
        splits.append(Document(
            metadata=doc.metadata,
            page_content=chunk
        ))

# 데이터를 벡터화하여 vector db에 저장
embeddings = UpstageEmbeddings(
    api_key=os.getenv('UPSTAGE_API_KEY'),
    model=os.getenv('EMBEDDING_MODEL_NAME')
)

vector_store = Chroma(embedding_function=embeddings)
vector_store.add_documents(splits)
retriever = vector_store.as_retriever(
    search_type="similarity",
    search_kwargs={"k": 1}
)

# 언어 모델 생성
llm = ChatUpstage(
    api_key=os.getenv('UPSTAGE_API_KEY'),
    model=os.getenv('LANG_MODEL_NAME'),
    temperature=0
)

# 프롬프트 템플릿 설정
prompt_template = PromptTemplate.from_template(
    """
    ## 역할 및 지침
    - 한국장기조직기증원(KODA)의 상담 챗봇으로서 사용자의 질문에 항상 **정중하고 예의있는 어투**로 응대해 주세요.
    - ** '안녕하세요', '감사합니다'와 같은 가벼운 인사말은 응대합니다. **
    - 프롬프트의 내용에 맞춰 체계적으로 답변하되, 조건문('~다면', '~일 때는')에 해당할 때만 오직 조건문 이후의 내용만을 안내해 주세요.
    - 본부명, 1차/2차 하위부서/직급, 전화번호는 절대 가공하지 않고 정확하게 답변합니다.
    - 답변은 최대 150~300자 내외로 유지합니다.
    
    ## 답변 생성 규칙
    - 아래 내용은 사용자의 질문에 답변을 생성하기 위한 내부 정보이니, 최종 답변에 직접 노출하지 마세요.
    - 사용자 질문 키워드가 아래 [내부 참고자료]와 관련 있다면, 해당 본부명, 1차/2차 하위부서/직급과 담당 업무, 전화번호를 안내해 주세요.
    - ** [내부 참고자료]에 '권역별 지부'인 내용이 포함되어 있고 사용자 질문에 특정 지역(중부, 충청호남, 영남)이 명시되지 않았다면, 답변의 마지막에 다음 문구를 추가하세요: **
        "어느 지부로 문의하시는지 알려주시면 더욱 정확한 안내를 드릴 수 있습니다."
    - **사용자 질문이 한국장기조직기증원(KODA)의 업무(경영관리, 기증관리 등)와 관련이 없을 경우, 다음과 같이 완곡하게 표현해 주세요:**
        "안녕하세요! 한국장기조직기증원(KODA)입니다. 문의해 주셔서 감사합니다. 현재 질문 주신 내용은 저희 챗봇이 답변할 수 있는 분야와 다소 거리가 있어 자세한 안내가 어렵습니다. 장기 기증과 관련된 일반적인 문의가 있으시다면, KODA 홈페이지를 방문하시거나, 대표 전화번호(02-3447-5632) 또는 채널톡, 카카오톡/인스타그램을 통해 문의해 주시면 친절하게 안내해 드리겠습니다. 감사합니다."
        *이 답변은 챗봇이 다른 컨텍스트와 무관한 질문일 때만 사용하며, 사용자에게 '관련 없다'는 식의 부정적 어조를 피하고 다른 안내 채널을 적극적으로 제공합니다.*
    
    ## 사용자 질문:
    {question}
    
    ## 내부 참고자료:
    {context}
    
    안녕하세요, 한국장기조직기증원(KODA)입니다.
    """)

# 체인 생성
chain = prompt_template | llm

# 챗봇 답변 응답 API 구현
@router.post("/")
async def chat(chat: ChatInput):
    try:
        # 1. 400 Bad Request 처리: 입력 유효성 검사
        if len(chat.question.strip()) < 1:
            raise HTTPException(status_code=400, detail="빈 공백만 입력할 수 없습니다. 좀 더 구체적으로 입력해 주세요.")

        # 2. 404 Not Found 처리: 컨텍스트 검색
        context = retriever.invoke(chat.question)
        if not context:
            raise HTTPException(status_code=404, detail="관련된 문서를 찾을 수 없습니다.")

            # 3. 404 Not Found 처리: 관련된 원본 문서 가져오기
        for doc in docs:
            if doc.metadata['doc_id'] == context[0].metadata['doc_id']:
                post_context = doc.page_content
                result = chain.invoke({'question': chat.question, 'context': post_context})
                return {"message": result.content}

        raise HTTPException(status_code=404, detail="원본 문서에서 일치하는 문서를 찾지 못 했습니다.") # 정상적인 동작이라면 untouchable error

    # 4.500 Internal Server Error 처리: 예상치 못 한 모든 오류
    except Exception:
        raise HTTPException(status_code=500, detail="서버에 일시적인 문제가 발생했습니다.")