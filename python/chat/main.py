from fastapi import FastAPI
from chatbot import router

app = FastAPI()

app.include_router(router)