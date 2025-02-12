import requests
import json

def main():
    url = "http://localhost:8000/generate"
    payload = {
        "messages": [
            {"role": "user", "content": "Привет, как тебя зовут?"},
            {"role": "assistant", "content": "Меня зовут Qwen."},
            {"role": "user", "content": "Расскажи о квантовании."}
        ]
    }
    headers = {"Content-Type": "application/json"}
    
    # Отправляем запрос в режиме stream
    with requests.post(url, headers=headers, data=json.dumps(payload), stream=True) as response:
        if response.status_code != 200:
            print("Request failed with status code:", response.status_code)
            return

        # Считываем данные по мере поступления и выводим их
        for chunk in response.iter_content(chunk_size=None):
            if chunk:
                print(chunk.decode("utf-8"), end="")

if __name__ == "__main__":
    main()