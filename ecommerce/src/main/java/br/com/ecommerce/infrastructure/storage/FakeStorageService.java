package br.com.ecommerce.infrastructure.storage;

import br.com.ecommerce.service.common.Storage;
import br.com.ecommerce.service.common.StorageFile;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

@Component
@ConditionalOnProperty(value = "ecommerce.storage.provider", havingValue="fake")
public class FakeStorageService implements Storage {
    @Override
    public List<StorageFile> upload(MultipartFile[] files) {
        if (files == null) {
            return List.of();
        }
        return Stream.of(files)
                .filter(Objects::nonNull)
                .filter(multipartFile -> multipartFile.getOriginalFilename() != null)
                .map(multipartFile -> {
                    String fileName = String.format("%s.%s", UUID.randomUUID(), getFileExtension(multipartFile.getOriginalFilename()));
                    return new StorageFile(fileName, "https://ecommerce_s3.com/attachments/%s/%s".formatted("file", fileName), OffsetDateTime.now(ZoneId.of("UTC")));
                })
                .toList();
    }

    private String getFileExtension(String fileName) {
        String[] split = fileName.split("\\.");
        return split[split.length - 1];
    }
}