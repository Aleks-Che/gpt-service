-- Modify existing t_user table to add subscription
ALTER TABLE t_user
ADD COLUMN current_subscription_id BIGINT,
ADD COLUMN subscription_start_date TIMESTAMP WITHOUT TIME ZONE,
ADD COLUMN subscription_end_date TIMESTAMP WITHOUT TIME ZONE;

-- Create subscription table
CREATE TABLE t_subscription (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(2048),
    price DECIMAL(10,2),
    duration_days INTEGER,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_subscription PRIMARY KEY (id)
);

-- Create LLM model table
CREATE TABLE t_llm_model (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(2048),
    model_type VARCHAR(50) NOT NULL, -- EXTERNAL, LOCAL
    provider VARCHAR(100), -- OPENAI, ANTHROPIC, LOCAL etc
    api_endpoint VARCHAR(255),
    api_token VARCHAR(255),
    max_tokens INTEGER,
    temperature DECIMAL(3,2),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_llm_model PRIMARY KEY (id)
);

-- Create subscription_model_access table (many-to-many with limits)
CREATE TABLE t_subscription_model_access (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    subscription_id BIGINT NOT NULL,
    model_id BIGINT NOT NULL,
    monthly_request_limit INTEGER,
    max_tokens_per_request INTEGER,
    max_file_size_mb INTEGER,
    CONSTRAINT pk_subscription_model_access PRIMARY KEY (id),
    CONSTRAINT fk_sma_subscription FOREIGN KEY (subscription_id) REFERENCES t_subscription(id),
    CONSTRAINT fk_sma_model FOREIGN KEY (model_id) REFERENCES t_llm_model(id)
);

CREATE TABLE t_folder (
                         id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                         name VARCHAR(255),
                         user_id BIGINT,
                         created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE t_chat (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    user_id BIGINT NOT NULL,
    folder_id BIGINT REFERENCES t_folder(id),
    title VARCHAR(255),
    model_id BIGINT NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_chat PRIMARY KEY (id),
    CONSTRAINT fk_chat_user FOREIGN KEY (user_id) REFERENCES t_user(id),
    CONSTRAINT fk_chat_model FOREIGN KEY (model_id) REFERENCES t_llm_model(id)
);

CREATE TABLE t_message (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    chat_id BIGINT NOT NULL,
    message_type VARCHAR(50) NOT NULL, -- REQUEST, RESPONSE
    message_status VARCHAR(50) NOT NULL, -- FINISHED, INCOMPLETE
    content TEXT NOT NULL,
    tokens_count INTEGER,
    content_summarize TEXT,
    summarize_tokens_count INTEGER,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    file_path VARCHAR(1024),
    file_size_bytes BIGINT,
    file_mime_type VARCHAR(255),
    CONSTRAINT pk_message PRIMARY KEY (id),
    CONSTRAINT fk_message_chat FOREIGN KEY (chat_id) REFERENCES t_chat(id)
);

-- Create usage_statistics table
CREATE TABLE t_usage_statistics (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_id BIGINT NOT NULL,
    model_id BIGINT NOT NULL,
    request_count INTEGER DEFAULT 0,
    total_tokens_used BIGINT DEFAULT 0,
    period_start_date TIMESTAMP WITHOUT TIME ZONE,
    period_end_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_usage_statistics PRIMARY KEY (id),
    CONSTRAINT fk_statistics_user FOREIGN KEY (user_id) REFERENCES t_user(id),
    CONSTRAINT fk_statistics_model FOREIGN KEY (model_id) REFERENCES t_llm_model(id)
);

-- Insert default free subscription
INSERT INTO t_subscription (name, description, price, duration_days)
VALUES ('Free', 'Basic free subscription', 0.00, 30);

-- Insert default model (example)
INSERT INTO t_llm_model (name, description, model_type, provider, max_tokens, temperature)
VALUES ('GPT-3.5-Turbo', 'OpenAI GPT-3.5 Turbo Model', 'EXTERNAL', 'OPENAI', 4096, 0.7);

-- Insert default model (example)
INSERT INTO t_llm_model (name, description, model_type, provider, max_tokens, temperature)
VALUES ('Qwen2.5-Coder', 'Qwen2.5-Coder-7B-Instruct', 'LOCAL', 'ALIBABA', 2048, 0.7);

-- Set up free subscription access to GPT-3.5
INSERT INTO t_subscription_model_access (subscription_id, model_id, monthly_request_limit, max_tokens_per_request, max_file_size_mb)
VALUES (1, 1, 200, 4096, 10);
