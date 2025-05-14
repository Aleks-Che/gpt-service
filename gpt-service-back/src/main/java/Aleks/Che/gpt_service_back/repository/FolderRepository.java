package Aleks.Che.gpt_service_back.repository;

import Aleks.Che.gpt_service_back.model.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
    List<Folder> findAllByUserId(Long userId);
    Optional<Folder> findByIdAndUserId(Long id, Long userId);
}
