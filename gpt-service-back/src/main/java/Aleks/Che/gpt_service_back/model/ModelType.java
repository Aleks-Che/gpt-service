package Aleks.Che.gpt_service_back.model;

public enum ModelType {
    EXTERNAL("Внешняя модель"),
    LOCAL("Локальная модель"),
    API("API модель"),
    FINE_TUNED("Дообученная модель");

    private final String displayName;

    ModelType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
