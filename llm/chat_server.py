import gradio as gr
from transformers import AutoModelForCausalLM, AutoTokenizer, BitsAndBytesConfig
import torch

torch.backends.cuda.matmul.allow_tf32 = True
torch.backends.cudnn.benchmark = True
torch.backends.cudnn.deterministic = False

# Инициализация модели
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



def batch_generate(prompts, batch_size=8):
    responses = []
    for i in range(0, len(prompts), batch_size):
        batch = prompts[i:i + batch_size]
        inputs = tokenizer(batch, return_tensors="pt", padding=True).to(model.device)
        output_ids = model.generate(
            **inputs,
            max_new_tokens=1024,  # Уменьшаем для быстрой генерации
            temperature=0.7,
            top_p=0.95,
            num_beams=1,  # Отключаем beam search для максимальной скорости
            do_sample=False,  # Отключаем sampling для детерминированной генерации
            use_cache=True,
            repetition_penalty=1.1,
            early_stopping=True  # Добавляем early stopping
        )
        responses.extend(tokenizer.batch_decode(output_ids[:, inputs.input_ids.shape[1]:], skip_special_tokens=True))
    return responses


# Пример использования в generate_response
def generate_response(message, history):
    messages = [
        {"role": "system", "content": "You are Qwen, created by Alibaba Cloud. You are a helpful assistant."}
    ]
    
    # Собираем все сообщения в список для batch processing
    all_messages = []
    for human, assistant in history:
        all_messages.append({"role": "user", "content": human})
        all_messages.append({"role": "assistant", "content": assistant})
    all_messages.append({"role": "user", "content": message})
    
    # Подготавливаем тексты для batch processing
    texts = [tokenizer.apply_chat_template(messages + [msg], tokenize=False, add_generation_prompt=True) 
            for msg in all_messages]
    
    # Генерируем ответы пакетами
    responses = batch_generate(texts)
    
    return responses[-1]  # Возвращаем последний ответ

# Создание веб-интерфейса
demo = gr.ChatInterface(
    generate_response,
    title="Qwen Code Assistant",
    description="Chat with Qwen2.5-Coder-32B model",
    examples=["Write a Python function for bubble sort", 
             "Explain how async/await works in Python",
             "Debug this code: def factorial(n): return n * factorial(n-1)"],
    theme="soft"
)

# Запуск сервера
if __name__ == "__main__":
    demo.launch(share=True, server_name="0.0.0.0")
