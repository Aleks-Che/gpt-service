package Aleks.Che.gpt_service_back.model;

public enum MessageType {
    REQUEST,    // Запрос пользователя
    RESPONSE,   // Ответ модели
    SYSTEM,     // Системное сообщение
    ERROR       // Сообщение об ошибке
}
