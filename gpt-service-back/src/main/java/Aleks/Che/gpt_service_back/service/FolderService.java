package Aleks.Che.gpt_service_back.service;

import Aleks.Che.gpt_service_back.exception.ResourceNotFoundException;
import Aleks.Che.gpt_service_back.model.Folder;
import Aleks.Che.gpt_service_back.model.user.User;
import Aleks.Che.gpt_service_back.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderService {
    private final FolderRepository folderRepository;

    public Folder createFolder(String name, User user) {
        Folder folder = new Folder();
        folder.setName(name);
        folder.setUser(user);
        folder.setCreatedAt(LocalDateTime.now());
        return folderRepository.save(folder);
    }

    public Folder updateFolder(Long id, String name, Long userId) {
        Folder folder = folderRepository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new ResourceNotFoundException("Folder not found"));
        folder.setName(name);
        return folderRepository.save(folder);
    }

    public void deleteFolder(Long id, Long userId) {
        Folder folder = folderRepository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new ResourceNotFoundException("Folder not found"));
        folderRepository.delete(folder);
    }

    public List<Folder> getUserFolders(Long userId) {
        return folderRepository.findAllByUserId(userId);
    }

    public Folder getFolderWithChats(Long id, Long userId) {
        return folderRepository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new ResourceNotFoundException("Folder not found"));
    }
}
