import logging
from contextlib import asynccontextmanager

from fastapi import FastAPI, Request
from starlette.responses import JSONResponse

from .api.reply import router
from .core.exceptions import GeneralError, CustomBaseError
from .core.services import prepare_chat_data
from .schemas.response import ApiResponse

logging.basicConfig(
	format='%(asctime)s %(levelname)s %(name)s %(message)s',
	datefmt='%Y-%m-%d %H:%M:%S',
	level=logging.INFO)

logger = logging.getLogger(__name__)


@asynccontextmanager
async def lifespan(app: FastAPI):
	# Startup tasks
	logger.info('[chatbot] FastAPI 챗봇 애플리케이션 실행')
	await prepare_chat_data(app)

	yield
	# Shutdown tasks
	logger.info('[chatbot] FastAPI 챗봇 애플리케이션 종료')


app = FastAPI(lifespan=lifespan)
app.include_router(router)


@app.exception_handler(CustomBaseError)
async def custom_base_error_handler(request: Request, exc: CustomBaseError):
	""" 사용자 정의 예외를 처리하는 전역 예외 처리기를 정의합니다.

	Args:
		request(Request): 사용자 요청 객체.
		exc(CustomBaseError): 발생한 예외 객체.

	Returns:
		JSONResponse: JSON 형식의 응답 객체.
					  HTTP 상태 코드와 ApiResponse(응답 상태코드, 사용자 메시지)를 포함합니다.
					  반환 예시:
					  {
						"success": false,
						"code": 500,
						"message": "DB 서버 연결 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.",
						"data": null
					  }
	"""

	logger.error(
		f"[{request.method} {request.url.path}] {exc.developer_message}: "
		f"{exc.details}")
	return JSONResponse(
		status_code=exc.code,
		content=ApiResponse.error(exc.code, exc.user_message).model_dump()
	)


@app.exception_handler(GeneralError)
async def internal_server_error_handler(request: Request, exc: GeneralError):
	""" 사용자 정의 예외를 처리하지 못한 그 외 모든 예외를 처리합니다.

	Args:
		request(Request): 사용자 요청 객체.
		exc(GeneralError): 발생한 예외 객체.

	Returns:
		JSONResponse: JSON 형식의 응답 객체.
					  HTTP 상태 코드와 ApiResponse(응답 상태코드, 사용자 메시지)를 포함합니다.
					  반환 예시:
					  {
						"success": false,
						"code": 500,
						"message": "죄송합니다. 서버에 일시적인 문제가 발생했습니다.",
						"data": null
					  }
	"""

	logger.error(f"[{request.method} {request.url.path}] {str(exc)}",
				 exc_info=exc)
	return JSONResponse(
		status_code=exc.code,
		content=ApiResponse.internal_server_error().model_dump()
	)
