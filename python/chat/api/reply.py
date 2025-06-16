import logging
from http import HTTPStatus

from fastapi import APIRouter, Request
from starlette.responses import JSONResponse

from ..core.exceptions import BadRequestError, DocumentNotFoundError
from ..models.chat import ChatInput
from ..schemas.response import ApiResponse

logger = logging.getLogger(__name__)
logger.setLevel(logging.INFO)

# 라우터 생성
router = APIRouter(
	prefix="/chat",
	tags=['chat']
)


@router.post("/")
async def reply(chat: ChatInput, request: Request):
	""" 사용자 입력에 대하여 챗봇 응답을 반환하는 API를 구현합니다.

	Args:
		chat(ChatInput): 사용자 입력과 관련된 데이터를 포함하는 객체.
		request(Request): 요청 객체.

	Returns:
		JSONResponse: JSON 형식의 응답 객체.
					  HTTP 상태 코드와 ApiResponse(응답 상태코드, 사용자 메시지, 데이터)를 포함합니다.
				 	  반환 예시:
					  {
					   	"success": true,
						"code": 200,
						"message": "응답이 성공적으로 반환되었습니다.",
						"data": {
							"message": "안녕하세요! 한국장기조직기증원(KODA)입니다. 어떻게
							도와드릴까요?"
						}
					  }

	Raises:
		BadRequestError: 사용자 입력 유효성 검사 실패할 경우.
		DocumentNotFoundError: 사용자 질의와 관련된 문서를 찾지 못할 경우.
							   사용자 질의로 검색 시 반환 결과가 없는 경우.
							   검색 결과로 반환된 문서의 원본 문서를 탐색하지 못할 경우.
	"""

	# 서버 로딩 때 저장한 값 가져오기
	retriever = request.app.state.retriever
	docs = request.app.state.docs
	chain = request.app.state.chain

	question = chat.question
	# 1. 입력 유효성 검사
	if not question or not question.strip():
		raise BadRequestError(details={'question': question})
	logger.info(f"[INPUT_VALIDATION_SUCCESS] 챗봇 입력 유효성 검사 성공: \"{question}\"")

	# 2. 컨텍스트 검색
	context = retriever.invoke(question)
	if not context:
		raise DocumentNotFoundError(details={'reason': '검색 반환 결과 없음'})
	logger.info('[DB DOCS] 챗봇 문서 불러오기 성공')

	# 3. 원본 문서 가져오기
	for og_doc in docs:
		if og_doc.metadata['doc_id'] == context[0].metadata['doc_id']:
			post_context = og_doc.page_content
			result = chain.invoke(
				{'question': question, 'context': post_context})
			return JSONResponse(
				status_code=HTTPStatus.OK,
				content=ApiResponse.ok(
					{"message": result.content}).model_dump()
			)
	raise DocumentNotFoundError(
		details={'reason': 'untouchable 오류 발생, 원본 문서 탐색 실패'})