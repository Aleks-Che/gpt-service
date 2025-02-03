package Aleks.Che.gpt_service_back.repository;

import Aleks.Che.gpt_service_back.model.UsageStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsageStatisticsRepository extends JpaRepository<UsageStatistics, Long> {
    Optional<UsageStatistics> findByUserIdAndModelIdAndPeriodStartDateLessThanEqualAndPeriodEndDateGreaterThanEqual(
            Long userId, Long modelId, LocalDateTime currentDate, LocalDateTime currentDate2);
        
    List<UsageStatistics> findByUserIdAndPeriodStartDateBetween(
        Long userId, LocalDateTime startDate, LocalDateTime endDate);
}
