package Aleks.Che.gpt_service_back.repository;

import Aleks.Che.gpt_service_back.model.ChatEntity;
import Aleks.Che.gpt_service_back.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, Long> {
    List<ChatEntity> findByUserOrderByUpdatedAtDesc(User user);
    List<ChatEntity> findByUserAndModelId(User user, Long modelId);
    List<ChatEntity> findByUserAndIsArchivedFalseOrderByUpdatedAtDesc(User user);
    void deleteByUserAndId(User user, Long id);
    
    @Query("SELECT c FROM ChatEntity c WHERE c.user = :user AND " +
           "(LOWER(c.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<ChatEntity> searchUserChats(@Param("user") User user,
                                             @Param("searchTerm") String searchTerm);
    Long findMostUsedModelByUserId(Long id);
}
