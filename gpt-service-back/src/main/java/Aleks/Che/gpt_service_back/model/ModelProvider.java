package Aleks.Che.gpt_service_back.model;

public enum ModelProvider {
    OPENAI("OpenAI"),
    ANTHROPIC("Anthropic"),
    GOOGLE("Google"),
    META("Meta"),
    LOCAL("Local"),
    CUSTOM("Custom");

    private final String displayName;

    ModelProvider(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
