from transformers import AutoModelForCausalLM, AutoTokenizer

model = AutoModelForCausalLM.from_pretrained("Qwen/Qwen2.5-Coder-32B-Instruct-GGUF")
tokenizer = AutoTokenizer.from_pretrained("Qwen/Qwen2.5-Coder-32B-Instruct-GGUF")

# Сохранить локально
model.save_pretrained("./models/Qwen2.5-Coder-32B-Instruct-GGUF")
tokenizer.save_pretrained("./models/Qwen2.5-Coder-32B-Instruct-GGUF")
