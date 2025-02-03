package Aleks.Che.gpt_service_back.repository;

import Aleks.Che.gpt_service_back.model.Conversation;
import Aleks.Che.gpt_service_back.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    List<Conversation> findByUserOrderByUpdatedAtDesc(User user);
    List<Conversation> findByUserAndModelId(User user, Long modelId);
    List<Conversation> findByUserAndIsArchivedFalseOrderByUpdatedAtDesc(User user);
    void deleteByUserAndId(User user, Long id);
    
    @Query("SELECT c FROM Conversation c WHERE c.user = :user AND " +
           "(LOWER(c.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Conversation> searchUserConversations(@Param("user") User user,
                                             @Param("searchTerm") String searchTerm);
    Long findMostUsedModelByUserId(Long id);
}
