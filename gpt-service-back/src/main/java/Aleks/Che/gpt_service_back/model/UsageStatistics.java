package Aleks.Che.gpt_service_back.model;

import Aleks.Che.gpt_service_back.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_usage_statistics")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsageStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "model_id")
    private LlmModel model;

    private Integer requestCount;
    private Long totalTokensUsed;
    private LocalDateTime periodStartDate;
    private LocalDateTime periodEndDate;
    
    @Column(name = "total_successful_requests")
    private Integer totalSuccessfulRequests;
    
    @Column(name = "total_failed_requests")
    private Integer totalFailedRequests;
    
    @Column(name = "average_response_time_ms")
    private Long averageResponseTimeMs;
    
    @Column(name = "total_files_processed")
    private Integer totalFilesProcessed;
    
    @Column(name = "total_file_size_bytes")
    private Long totalFileSizeBytes;
    
    @Column(name = "last_request_timestamp")
    private LocalDateTime lastRequestTimestamp;
}
