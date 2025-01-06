package br.com.ecommerce.service.category;

import br.com.ecommerce.domain.common.validation.ValidationError;
import br.com.ecommerce.domain.exception.ValidationException;
import br.com.ecommerce.domain.model.category.Category;
import br.com.ecommerce.domain.model.category.CategoryStatus;
import br.com.ecommerce.service.category.command.CreateCategoryCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CreateCategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    private CreateCategoryService createCategoryService;

    @BeforeEach
    public void setUp() {
        this.createCategoryService = new CreateCategoryService(categoryRepository);
    }

    @Test
    void shouldNotSaveCategoryWhenParentDoesNotExist() {
        Mockito.when(categoryRepository.findById(10L))
                .thenReturn(Optional.empty());
        CreateCategoryCommand createCategoryCommand = new CreateCategoryCommand("Category name", 10L);
        ValidationException validationException = Assertions.assertThrows(ValidationException.class, () -> createCategoryService.save(createCategoryCommand));

        Assertions.assertEquals(List.of("parent.category.not.found"), validationException.getErrors().getErrors().stream().map(ValidationError::code).toList());
        Mockito.verify(categoryRepository, Mockito.never()).add(Mockito.any(Category.class));
    }

    @Test
    void shouldAssignParentAndSaveCategory() {
        Category category = new Category("Sports");
        Mockito.when(categoryRepository.findById(10L))
                .thenReturn(Optional.of(category));
        Category persisted = createCategoryService.save(new CreateCategoryCommand("Running", 10L));

        Assertions.assertEquals(category, persisted.getParent());
        Assertions.assertEquals("Running", persisted.getName());
        Assertions.assertEquals(CategoryStatus.ACTIVE, persisted.getCategoryStatus());
        Assertions.assertNotNull(persisted.getCreatedAt());
        Mockito.verify(categoryRepository, Mockito.times(1)).add(persisted);
    }

    @Test
    void shouldAssignSaveCategoryWithoutParent() {
        Category persisted = createCategoryService.save(new CreateCategoryCommand("Running", null));

        Assertions.assertNull(persisted.getParent());
        Assertions.assertEquals("Running", persisted.getName());
        Assertions.assertEquals(CategoryStatus.ACTIVE, persisted.getCategoryStatus());
        Assertions.assertNotNull(persisted.getCreatedAt());
        Mockito.verify(categoryRepository, Mockito.times(1)).add(persisted);
    }
}