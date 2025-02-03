from transformers import AutoModelForCausalLM, AutoTokenizer, BitsAndBytesConfig

# Настройка 4-битной квантизации
quantization_config = BitsAndBytesConfig(
    load_in_4bit=True,
    bnb_4bit_compute_dtype="float16",
    bnb_4bit_quant_type="nf4",
    bnb_4bit_use_double_quant=True
)
# Загрузка модели с квантизацией
model = AutoModelForCausalLM.from_pretrained(
    "Qwen/Qwen2.5-Coder-32B-Instruct",
    quantization_config=quantization_config,
    device_map="auto"
)
tokenizer = AutoTokenizer.from_pretrained("Qwen/Qwen2.5-Coder-32B-Instruct")

# Пример запроса
messages = [
    {"role": "system", "content": "You are Qwen, created by Alibaba Cloud. You are a helpful assistant."},
    {"role": "user", "content": "Write a Python function to calculate fibonacci numbers."}
]

# Подготовка входных данных
text = tokenizer.apply_chat_template(messages, tokenize=False, add_generation_prompt=True)
inputs = tokenizer([text], return_tensors="pt").to(model.device)

# Генерация
output_ids = model.generate(**inputs, max_new_tokens=512)
response = tokenizer.batch_decode(output_ids[:, inputs.input_ids.shape[1]:], skip_special_tokens=True)[0]
print(response)
