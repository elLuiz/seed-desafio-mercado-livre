package br.com.ecommerce.service.product;

import br.com.ecommerce.domain.model.product.Product;
import br.com.ecommerce.domain.model.product.ProductMedia;
import br.com.ecommerce.service.common.Storage;
import br.com.ecommerce.service.common.StorageFile;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class AttachImagesService {
    private final ProductRepository productRepository;
    private final Storage storage;

    public AttachImagesService(ProductRepository productRepository, Storage storage) {
        this.productRepository = productRepository;
        this.storage = storage;
    }

    @Transactional
    public void attachMedias(Long productId, MultipartFile[] files) {
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException("product.not.found"));
        List<StorageFile> storageFiles = storage.upload(files);
        storageFiles.forEach(storageFile -> product.addMedia(new ProductMedia(storageFile.fileName(), storageFile.url(), storageFile.uploadedAt())));
        this.productRepository.update(product);
    }
}