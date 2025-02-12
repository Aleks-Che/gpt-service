package Aleks.Che.gpt_service_back.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@AllArgsConstructor
public class ExecutorConfig {
    
    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(10);
    }
}
