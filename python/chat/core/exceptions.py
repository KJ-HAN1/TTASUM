from typing import Dict, Optional, Any


class CustomBaseError(Exception):
	""" 실행 코드에서 발생시킬 사용자 정의 예외를 정의합니다.

	Attributes:
		code(int): 상태 코드.
		user_message(str): 사용자에게 제공할 오류 메시지.
		developer_message(str): 개발자에게 제공할 로깅 메시지.
		details(Optional[Dict[str, Any]]=None): 오류가 발생한 컨텍스트.
	"""

	def __init__(self, code: int, user_message: str, developer_message: str,
				 details: Optional[Dict[str, Any]] = None):
		super().__init__(user_message)  # 예외 기본 메시지 설정
		self.code = code
		self.user_message = user_message
		self.developer_message = developer_message
		self.details = details

	def __str__(self):
		return (f"code: {self.code}, developer_message: "
				f"{self.developer_message}, user_message: "
				f"{self.user_message}, details: {self.details}")


class DBConnectionError(CustomBaseError):
	"""런타임 중 DB 연결 실패 또는 쿼리 실행 중에 발생하는 오류입니다."""

	def __init__(self, details):
		super().__init__(
			code=500,
			user_message="DB 서버 연결 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.",
			developer_message=f"DB 연결 실패 또는 쿼리 실행 중 오류 발생",
			details=details
		)


class BadRequestError(CustomBaseError):
	"""사용자 입력 유효성 검사에 실패할 때 발생하는 오류입니다."""

	def __init__(self, details: Dict[str, Any]):
		super().__init__(
			code=400,
			user_message="빈 공백만 입력할 수 없습니다. 좀 더 구체적으로 입력해 주세요.",
			developer_message="사용자 입력 유효성 검사 실패",
			details=details
		)


class DocumentNotFoundError(CustomBaseError):
	"""사용자 질의와 관련된 문서를 찾지 못할 때 발생하는 오류입니다."""

	def __init__(self, details):
		super().__init__(
			code=404,
			user_message="질의와 관련된 문서를 찾을 수 없습니다.",
			developer_message="질의 관련 문서 탐색 실패",
			details=details
		)


class GeneralError(CustomBaseError):
	"""사용자 정의 예외 외의 예상치 못한 모든 오류가 발생할 때 사용하는 오류입니다."""

	def __init__(self):
		super().__init__(
			code=500,
			user_message="죄송합니다. 서버에 일시적인 문제가 발생했습니다.",
			developer_message="예상치 못한 오류 시스템 오류 발생"
		)
