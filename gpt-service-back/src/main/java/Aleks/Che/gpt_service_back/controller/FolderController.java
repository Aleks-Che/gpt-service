package Aleks.Che.gpt_service_back.controller;

import Aleks.Che.gpt_service_back.dto.FolderRequest;
import Aleks.Che.gpt_service_back.model.Folder;
import Aleks.Che.gpt_service_back.model.user.User;
import Aleks.Che.gpt_service_back.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/folders")
@RequiredArgsConstructor
public class FolderController {
    private final FolderService folderService;

    @PostMapping
    public ResponseEntity<Folder> createFolder(@RequestBody FolderRequest request, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(folderService.createFolder(request.getName(), user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Folder> updateFolder(
            @PathVariable Long id,
            @RequestBody FolderRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(folderService.updateFolder(id, request.getName(), user.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFolder(@PathVariable Long id, @AuthenticationPrincipal User user) {
        folderService.deleteFolder(id, user.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Folder>> getUserFolders(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(folderService.getUserFolders(user.getId()));
    }

    @GetMapping("/{id}/chats")
    public ResponseEntity<Folder> getFolderWithChats(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(folderService.getFolderWithChats(id, user.getId()));
    }
}
