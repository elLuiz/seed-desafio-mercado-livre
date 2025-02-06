package br.com.ecommerce.service.category;

import br.com.ecommerce.domain.common.validation.ValidationErrors;
import br.com.ecommerce.domain.exception.ValidationException;
import br.com.ecommerce.domain.model.category.Category;
import br.com.ecommerce.service.category.command.CreateCategoryCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateCategoryService {
    private final CategoryRepository categoryRepository;

    public CreateCategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public Category save(CreateCategoryCommand createCategoryCommand) {
        Category category = createCategoryCommand.toModel();
        if (createCategoryCommand.parentId() != null) {
            Category parent = categoryRepository.findById(createCategoryCommand.parentId()).orElseThrow(() -> new ValidationException(ValidationErrors.single("parentId", "parent.category.not.found")));
            category.childOf(parent);
        }
        categoryRepository.add(category);
        return category;
    }
}