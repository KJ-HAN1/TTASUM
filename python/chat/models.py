from pydantic import BaseModel

class ChatInput(BaseModel):
    question: str # 키 값 일치