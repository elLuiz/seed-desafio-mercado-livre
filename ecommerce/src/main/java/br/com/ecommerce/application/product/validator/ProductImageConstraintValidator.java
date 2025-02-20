package br.com.ecommerce.application.product.validator;

import br.com.ecommerce.domain.common.validation.ValidationErrors;
import br.com.ecommerce.util.Either;
import br.com.ecommerce.util.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class ProductImageConstraintValidator {
    private static final List<String> FORMATS = List.of("png", "jpeg", "jpg");
    private static final long MAX_FILES = 5; 
    private static final long MAX_FILE_SIZE_IN_BYTES = 2_000_000;
    public static final String MEDIA_FIELD = "media";

    public Either<ValidationErrors, Boolean> checkFiles(MultipartFile[] files) {
        if (files == null) {
            ValidationErrors validationErrors = ValidationErrors.single(MEDIA_FIELD, "files.must.not.be.null");
            return Either.error(validationErrors);
        } else if (files.length > MAX_FILES) {
            ValidationErrors validationErrors = ValidationErrors.single(MEDIA_FIELD, "files.must.not.exceed.limit");
            return Either.error(validationErrors);
        }
        ValidationErrors validationErrors = new ValidationErrors();
        checkFormat(files, validationErrors);
        checkIndividualSize(files, validationErrors);
        checkTotalSize(files, validationErrors);
        return validationErrors.hasAnyError() ? Either.error(validationErrors) : Either.correct(true);
    }

    private void checkFormat(MultipartFile[] files, ValidationErrors validationErrors) {
        for (MultipartFile multipartFile : files) {
            if (multipartFile != null && (StringUtils.isNullOrEmpty(multipartFile.getOriginalFilename()) || FORMATS.stream().noneMatch(format -> multipartFile.getOriginalFilename().endsWith(format)))) {
                validationErrors.add(Objects.requireNonNullElse(multipartFile.getOriginalFilename(), MEDIA_FIELD), "invalid.media.format");
            }
        }
    }

    private void checkIndividualSize(MultipartFile[] files, ValidationErrors validationErrors) {
        for (MultipartFile multipartFile : files) {
            if (multipartFile != null && multipartFile.getSize() > MAX_FILE_SIZE_IN_BYTES) {
                validationErrors.add(Objects.requireNonNullElse(multipartFile.getOriginalFilename(), MEDIA_FIELD), "file.exceeds.max.size");
            }
        }
    }

    private void checkTotalSize(MultipartFile[] files, ValidationErrors validationErrors) {
        long totalSize = Arrays.stream(files).map(MultipartFile::getSize).reduce(0L, Long::sum);
        if (totalSize > MAX_FILE_SIZE_IN_BYTES * MAX_FILES) {
            validationErrors.add(MEDIA_FIELD, "request.exceeds.limit");
        }
    }
}