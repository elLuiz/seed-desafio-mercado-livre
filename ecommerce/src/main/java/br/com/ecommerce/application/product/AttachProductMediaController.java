package br.com.ecommerce.application.product;

import br.com.ecommerce.application.product.validator.ProductImageConstraintValidator;
import br.com.ecommerce.domain.exception.ValidationException;
import br.com.ecommerce.service.product.AttachImagesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/products")
public class AttachProductMediaController {
    private final ProductImageConstraintValidator productImageConstraintValidator;
    private final AttachImagesService attachImagesService;

    public AttachProductMediaController(ProductImageConstraintValidator productImageConstraintValidator, AttachImagesService attachImagesService) {
        this.productImageConstraintValidator = productImageConstraintValidator;
        this.attachImagesService = attachImagesService;
    }

    @PutMapping(value = "{productId}/attachments/medias", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('CREATE_PRODUCT') and @productPermissionValidator.isOwnerOf(#productId, #jwt)")
    public void attachMedia(@PathVariable("productId") Long productId,
                            @AuthenticationPrincipal Jwt jwt,
                            @RequestPart("media") MultipartFile[] files) {
        productImageConstraintValidator.checkFiles(files).orElseThrow(ValidationException::new);
        attachImagesService.attachMedias(productId, files);
    }
}