package br.com.ecommerce.infrastructure.storage;

import br.com.ecommerce.commons.MultipartFactory;
import br.com.ecommerce.service.common.StorageFile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

class FakeStorageServiceTest {
    FakeStorageService fakeStorageService = new FakeStorageService();

    @Test
    void shouldReturnEmptyWhenFilesAreNull() {
        List<StorageFile> files = fakeStorageService.upload(null);
        Assertions.assertTrue(files.isEmpty());
    }

    @Test
    void shouldReturnUploadedFiles() throws Exception {
        MockMultipartFile file = MultipartFactory.getMultipartFile("image.png", "src/test/resources/assets/img.png");
        List<StorageFile> files = fakeStorageService.upload(new MultipartFile[]{file});

        Assertions.assertAll(() -> {
            Assertions.assertEquals(1, files.size());
            Assertions.assertTrue(files.stream().map(StorageFile::fileName).allMatch(Objects::nonNull));
        });
    }
}