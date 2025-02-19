package br.com.ecommerce.service.product;

import br.com.ecommerce.commons.MultipartFactory;
import br.com.ecommerce.domain.model.category.Category;
import br.com.ecommerce.domain.model.product.Product;
import br.com.ecommerce.domain.model.product.ProductCharacteristic;
import br.com.ecommerce.domain.model.user.User;
import br.com.ecommerce.infrastructure.storage.FakeStorageService;
import br.com.ecommerce.service.common.Storage;
import br.com.ecommerce.service.common.StorageFile;
import jakarta.persistence.EntityNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AttachImagesServiceTest {
    @Mock
    ProductRepository productRepository;
    AttachImagesService attachImagesService;

    @BeforeEach
    void setUp() {
        this.attachImagesService = new AttachImagesService(productRepository, new FakeStorageService());
    }

    @Test
    void shouldAttachMediasToProduct() throws Exception {
        Product product = new Product(new Category("Programming"), "A brief description", BigDecimal.valueOf(20.9), "C#", 10, List.of(new ProductCharacteristic("Genre", "Science & Technology")), Mockito.mock(User.class));
        Mockito.when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));
        Mockito.doNothing().when(productRepository).update(product);
        MultipartFile[] files = getMultipartFiles();
        this.attachImagesService.attachMedias(1L, files);

        Assertions.assertEquals(2, product.getMedias().size());
        Assertions.assertTrue(product.getMedias().stream().allMatch(productMedia -> productMedia.getFileName().endsWith(".png")));
    }

    @Test
    void shouldThrowExceptionWhenProductDoesNotExist() throws Exception {
        Mockito.when(productRepository.findById(1L))
                .thenReturn(Optional.empty());

        MultipartFile[] files = getMultipartFiles();
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.attachImagesService.attachMedias(1L, files));
    }

    private static MultipartFile @NotNull [] getMultipartFiles() throws IOException {
        MultipartFile file = MultipartFactory.getMultipartFile("img.png", "src/test/resources/assets/img.png");
        MultipartFile file1 = MultipartFactory.getMultipartFile("img_1.png", "src/test/resources/assets/img_1.png");
        return new MultipartFile[]{file, file1};
    }
}