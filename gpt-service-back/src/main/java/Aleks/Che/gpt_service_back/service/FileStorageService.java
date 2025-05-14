package Aleks.Che.gpt_service_back.service;

import Aleks.Che.gpt_service_back.exception.StorageException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@Slf4j
public class FileStorageService {

    @Value("${file.upload.dir}")
    private String uploadDir;

    @Value("${file.max.size:10485760}") // 10MB по умолчанию
    private long maxFileSize;

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(uploadDir));
        } catch (IOException e) {
            log.error("Не удалось создать директорию для загрузки файлов: {}", e.getMessage());
            throw new StorageException("Не удалось создать директорию для загрузки файлов");
        }
    }

    public String storeFile(MultipartFile file) {
        validateFile(file);
        
        String fileName = generateUniqueFileName(file.getOriginalFilename());
        Path targetLocation = Paths.get(uploadDir).resolve(fileName);

        try {
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            log.error("Ошибка при сохранении файла {}: {}", fileName, e.getMessage());
            throw new StorageException("Не удалось сохранить файл " + fileName);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("Файл не найден: " + fileName);
            }
        } catch (MalformedURLException | FileNotFoundException e) {
            log.error("Ошибка при загрузке файла {}: {}", fileName, e.getMessage());
            throw new StorageException("Не удалось загрузить файл " + fileName);
        }
    }

    public void deleteFile(String fileName) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.error("Ошибка при удалении файла {}: {}", fileName, e.getMessage());
            throw new StorageException("Не удалось удалить файл " + fileName);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new StorageException("Невозможно сохранить пустой файл");
        }
        if (file.getSize() > maxFileSize) {
            throw new StorageException("Размер файла превышает допустимый предел");
        }
        // Дополнительные проверки безопасности файла
        String fileName = file.getOriginalFilename();
        if (fileName != null && fileName.contains("..")) {
            throw new StorageException("Недопустимое имя файла");
        }
    }

    private String generateUniqueFileName(String originalFileName) {
        String fileExtension = StringUtils.getFilenameExtension(originalFileName);
        return UUID.randomUUID().toString() + "." + fileExtension;
    }
}
