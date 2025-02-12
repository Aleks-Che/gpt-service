#!/usr/bin/env python3
import asyncio
from fastapi import FastAPI, Request, HTTPException
from fastapi.responses import PlainTextResponse, StreamingResponse
from pydantic import BaseModel
from typing import List, Optional
from llama_cpp import Llama
from fastapi.middleware.cors import CORSMiddleware
import json

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # Или список разрешённых источников
    allow_methods=["*"],
    allow_headers=["*"],
)

# Выбор устройства: "gpu" или "cpu"
DEVICE = "cpu"  # Установите "gpu" для использования GPU

# Настройка параметров в зависимости от выбранного устройства
n_gpu_layers = 100 if DEVICE == "gpu" else 0

# Путь к файлу модели в формате gguf.
MODEL_PATH = "./models/Qwen2.5-Coder-32B-Instruct-GGUF/qwen2.5-coder-32b-instruct-q8_0.gguf"

# Загружаем модель один раз при старте сервера.
llama_model = Llama(
    model_path=MODEL_PATH,
    n_ctx=32768,
    n_gpu_layers=n_gpu_layers,
    # Другие параметры (например, seed, temperature, top_p, repeat_penalty и пр.) можно добавить при необходимости.
)

# Форматы сообщений, приходящих от клиента
class Message(BaseModel):
    role: str
    content: str

# Формат запроса – список сообщений (история)
class ChatRequest(BaseModel):
    messages: List[Message]

def determine_max_tokens(prompt: str, default_max: int = 1024) -> int:
    prompt_tokens = len(llama_model.tokenize(prompt.encode("utf-8")))
    available_tokens = llama_model.n_ctx() - prompt_tokens
    return min(available_tokens, default_max)

def build_prompt(messages: List[Message]) -> str:
    """
    Собираем единый промпт из истории сообщений.
    Форматирование можно кастомизировать – например, отделять реплики специальными разделителями.
    В данном примере каждая реплика записывается в виде "Роль: сообщение".
    """
    lines = []
    for msg in messages:
        lines.append(f"{msg.role.capitalize()}: {msg.content}")
    return "\n".join(lines)

@app.post("/generate")
async def chat(request: Request, chat_req: ChatRequest):
    prompt = build_prompt(chat_req.messages)
    
    # Параметры генерации.
    max_tokens = determine_max_tokens(prompt, default_max=512)

    def generate_tokens():
        generation = llama_model(
            prompt=prompt,
            max_tokens=max_tokens,
            stream=True,
        )
        for token in generation:
            print("DEBUG token:", token)
            if isinstance(token, dict):
                text = token["choices"][0]["text"]
            else:
                text = token
            yield json.dumps({"content": text}) + "\n"

    async def token_generator():
        loop = asyncio.get_event_loop()
        for token in generate_tokens():
            if await request.is_disconnected():
                break
            yield token
            await asyncio.sleep(0)

    return StreamingResponse(token_generator(), media_type="text/plain")

# Новый endpoint для суммаризации текста
class SummarizationRequest(BaseModel):
    text: str
    mode: Optional[str] = "long"   # "short" или "long"

@app.post("/summarize", response_class=PlainTextResponse)
def summarize(s_req: SummarizationRequest):
    input_text = s_req.text.strip()
    mode = s_req.mode.lower()

    if mode == "short":
        prompt = (
            f"Сделай краткую суммаризацию данного текста, сократи его до 2-х слов:\n\n"
            f"{input_text}\n\nОтвет: Краткая суммаризация:"
        )
        max_tokens = 10
    elif mode == "long":
        prompt = (
            f"Сделай краткую суммаризацию данного текста, сократи его до 15 слов:\n\n"
            f"{input_text}\n\nОтвет: Краткая суммаризация:"
        )
        max_tokens = 50
    else:
        raise HTTPException(status_code=400, detail="mode должен быть 'short' или 'long'")

    generation = llama_model(
        prompt=prompt,
        max_tokens=max_tokens,
        stream=False,
    )
    result_text = generation["choices"][0]["text"].strip()
    words = result_text.split()
    if mode == "short":
        truncated = " ".join(words[:2])
    else:
        truncated = " ".join(words[:15])

    return truncated

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("server:app", host="0.0.0.0", port=8000, reload=True)
