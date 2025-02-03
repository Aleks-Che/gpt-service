import gradio as gr
from transformers import AutoModelForCausalLM, AutoTokenizer, BitsAndBytesConfig
import torch
import re

torch.backends.cuda.matmul.allow_tf32 = True
torch.backends.cudnn.benchmark = True
torch.backends.cudnn.deterministic = False

# Инициализация модели остается прежней
quantization_config = BitsAndBytesConfig(
    load_in_4bit=True,
    bnb_4bit_compute_dtype="float16",
    bnb_4bit_quant_type="nf4",
    bnb_4bit_use_double_quant=True
)

model = AutoModelForCausalLM.from_pretrained(
    "Qwen/Qwen2.5-Coder-32B-Instruct",
    quantization_config=quantization_config,
    device_map="auto"
)
tokenizer = AutoTokenizer.from_pretrained("Qwen/Qwen2.5-Coder-32B-Instruct")

def extract_code(text):
    """Извлекаем код из текста"""
    code_blocks = re.findall(r'```[\s\S]*?```', text)
    return code_blocks

def compress_message(message, compression_level='medium'):
    """Сжимаем сообщение с разной степенью сжатия"""
    # Сохраняем код
    code_blocks = extract_code(message)
    
    # Удаляем код из сообщения для сжатия
    for block in code_blocks:
        message = message.replace(block, '[CODE]')
    
    message_length = len(message)
    
    if compression_level == 'high':  # Для сообщений > 500
        keep_length = 100
    elif compression_level == 'medium':  # Для сообщений 350-500
        keep_length = 150
    else:  # Для сообщений 200-350
        keep_length = 200
        
    if message_length <= keep_length * 2:
        compressed = message
    else:
        half_length = keep_length // 2
        compressed = message[:half_length] + "..." + message[-half_length:]
    
    # Возвращаем код обратно
    for i, block in enumerate(code_blocks):
        compressed = compressed.replace('[CODE]', block, 1)
    
    return compressed

def compress_history(history, max_messages=3):
    """Сжимаем историю диалога"""
    compressed = []
    
    for human, assistant in history[-max_messages:]:
        # Определяем уровень сжатия на основе длины
        human_len = len(human)
        assistant_len = len(assistant)
        
        if human_len > 500:
            human = compress_message(human, 'high')
        elif human_len > 350:
            human = compress_message(human, 'medium')
        elif human_len > 200:
            human = compress_message(human, 'low')
            
        if assistant_len > 500:
            assistant = compress_message(assistant, 'high')
        elif assistant_len > 350:
            assistant = compress_message(assistant, 'medium')
        elif assistant_len > 200:
            assistant = compress_message(assistant, 'low')
            
        compressed.append((human, assistant))
    
    return compressed

def analyze_task(message):
    code_keywords = ["write", "code", "function", "implement", "class", "algorithm", "программу", "функцию", "написать", "создай", "создать", "напиши", "сделай", "сделать"]
    explain_keywords = ["explain", "how", "what", "why", "describe", "объясни", "как работает", "что такое", "расскажи"]
    debug_keywords = ["debug", "fix", "error", "problem", "issue", "ошибка", "исправить", "отладка"]
    
    message = message.lower()
    
    if any(keyword in message for keyword in code_keywords):
        return {"type": "code", "tokens": 2048}
    elif any(keyword in message for keyword in debug_keywords):
        return {"type": "debug", "tokens": 1024}
    elif any(keyword in message for keyword in explain_keywords):
        return {"type": "explain", "tokens": 1024}
    else:
        return {"type": "general", "tokens": 512}

def batch_generate(prompts, task_info, batch_size=8):
    responses = []
    for i in range(0, len(prompts), batch_size):
        batch = prompts[i:i + batch_size]
        inputs = tokenizer(batch, return_tensors="pt", padding=True).to(model.device)
        output_ids = model.generate(
            **inputs,
            max_new_tokens=task_info["tokens"],
            temperature=0.7,
            top_p=0.95,
            num_beams=1,
            do_sample=False,
            use_cache=True,
            repetition_penalty=1.1,
            early_stopping=True
        )
        responses.extend(tokenizer.batch_decode(output_ids[:, inputs.input_ids.shape[1]:], skip_special_tokens=True))
    return responses

def generate_response(message, history):
    task_info = analyze_task(message)
    compressed_history = compress_history(history)
    
    messages = [
        {"role": "system", "content": "You are Qwen, created by Alibaba Cloud. You are a helpful assistant."}
    ]
    
    all_messages = []
    for human, assistant in compressed_history:
        all_messages.append({"role": "user", "content": human})
        all_messages.append({"role": "assistant", "content": assistant})
    all_messages.append({"role": "user", "content": message})
    
    texts = [tokenizer.apply_chat_template(messages + [msg], tokenize=False, add_generation_prompt=True) 
            for msg in all_messages]
    
    responses = batch_generate(texts, task_info)
    
    return responses[-1]

# Создание веб-интерфейса
demo = gr.ChatInterface(
    generate_response,
    title="Qwen Code Assistant",
    description="Chat with Qwen2.5-Coder-32B model",
    examples=["Напиши на языке python пример алгоритма сортировка пузырьком", 
             "Объясни как в java работают асинхронные методы",
             "объсни работу кода: def factorial(n): return n * factorial(n-1)"],
    theme="soft",
    stop_btn=True
)

# Запуск сервера
if __name__ == "__main__":
    demo.launch(share=True, server_name="0.0.0.0")
