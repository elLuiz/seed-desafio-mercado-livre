package br.com.ecommerce.application.category;

import br.com.ecommerce.application.category.dto.CategoryCreatedResponse;
import br.com.ecommerce.application.common.ResourceCreatedResponse;
import br.com.ecommerce.domain.model.category.Category;
import br.com.ecommerce.service.category.CreateCategoryService;
import br.com.ecommerce.service.category.command.CreateCategoryCommand;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/categories")
public class CreateCategoryController {
    private final CreateCategoryService createCategoryService;

    public CreateCategoryController(CreateCategoryService createCategoryService) {
        this.createCategoryService = createCategoryService;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasAuthority('CREATE_CATEGORY')")
    public ResponseEntity<CategoryCreatedResponse> create(@RequestBody @Valid CreateCategoryCommand createCategoryCommand) {
        Category category = createCategoryService.save(createCategoryCommand);
        return ResourceCreatedResponse.created(CategoryCreatedResponse.toResponse(category), "/{id}", category.getId());
    }
}