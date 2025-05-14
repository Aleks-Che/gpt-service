package Aleks.Che.gpt_service_back.repository;

import Aleks.Che.gpt_service_back.model.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    boolean existsByUsername(String username);

    Optional<User> findById(Long id);
    List<User> findAll();
    Optional<User> findByUsername(String username);
    @Query("SELECT u FROM User u WHERE u.defaultLlm IS NULL OR u.defaultLlm = 0")
    List<User> findUsersWithoutDefaultModel();
}