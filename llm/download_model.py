import os
from huggingface_hub import snapshot_download

# Константы
MODEL_ID = "deepseek-ai/Janus-Pro-7B"
SAVE_DIR = "./models/deepseek-ai/Janus-Pro-7B"

# Создание директории, если она не существует
os.makedirs(SAVE_DIR, exist_ok=True)

# Загрузка модели с Hugging Face
snapshot_download(repo_id=MODEL_ID, local_dir=SAVE_DIR)
