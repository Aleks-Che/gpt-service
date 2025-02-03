package Aleks.Che.gpt_service_back.repository;

import Aleks.Che.gpt_service_back.model.Message;
import Aleks.Che.gpt_service_back.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByConversationIdOrderByCreatedAt(Long conversationId);
    
    @Query("SELECT m FROM Message m WHERE m.conversation.id = :conversationId " +
           "AND m.createdAt >= :startDate AND m.createdAt <= :endDate")
    List<Message> findByConversationAndDateRange(
        @Param("conversationId") Long conversationId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
    
    void deleteByConversationIdAndIdGreaterThan(Long conversationId, Long messageId);
    
    @Query("SELECT COUNT(m) FROM Message m WHERE m.conversation.user = :user " +
           "AND m.createdAt >= :startDate AND m.createdAt <= :endDate")
    long countUserMessagesInPeriod(
        @Param("user") User user,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
    
    @Query("SELECT SUM(m.tokensCount) FROM Message m WHERE m.conversation.user = :user " +
           "AND m.conversation.model.id = :modelId " +
           "AND m.createdAt >= :startDate AND m.createdAt <= :endDate")
    Long sumTokensByUserAndModelInPeriod(
        @Param("user") User user,
        @Param("modelId") Long modelId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
}
