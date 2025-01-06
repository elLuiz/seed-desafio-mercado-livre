package br.com.ecommerce.application.category;

import br.com.ecommerce.application.RequestSender;
import br.com.ecommerce.application.util.IntegrationTest;
import br.com.ecommerce.domain.model.category.Category;
import br.com.ecommerce.service.category.CategoryRepository;
import br.com.ecommerce.service.category.command.CreateCategoryCommand;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.stream.Stream;

@IntegrationTest
@Sql(scripts = "/insert-categories.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "/delete-categories.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
class CreateCategoryControllerTest extends RequestSender {
    @Autowired
    CategoryRepository categoryRepository;

    @ParameterizedTest
    @MethodSource("provideInvalidCategories")
    void shouldReturnBadRequestWhenThereIsInputError(CreateCategoryCommand createCategoryCommand, List<String> expectedErrors) throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en-US")
                .content(objectMapper.writeValueAsString(createCategoryCommand)));

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code", Matchers.containsInAnyOrder(expectedErrors.toArray())));
    }

    static Stream<Arguments> provideInvalidCategories() {
        return Stream.of(
                Arguments.of(new CreateCategoryCommand("Decoration".repeat(50), null), List.of("category.surpasses.limit")),
                Arguments.of(new CreateCategoryCommand("  ", -10L), List.of("category.must.not.be.empty", "invalid.parent.id")),
                Arguments.of(new CreateCategoryCommand(null, 10L), List.of("category.must.not.be.empty")),
                Arguments.of(new CreateCategoryCommand("Health & Lifestyle", null), List.of("category.name.must.be.unique"))
        );
    }

    @Test
    void shouldSaveCategoryWithParent() throws Exception {
        Long categoryId = categoryRepository.findByName("Programming")
                .map(Category::getId)
                .orElseThrow();
        CreateCategoryCommand createCategoryCommand = new CreateCategoryCommand("C#", categoryId);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en-US")
                .content(objectMapper.writeValueAsString(createCategoryCommand)));

        resultActions.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo("C#")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.parentCategory", Matchers.equalTo("Programming")));
    }

    @Test
    void shouldSaveCategoryWithoutParent() throws Exception {
        CreateCategoryCommand createCategoryCommand = new CreateCategoryCommand("Java", null);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en-US")
                .content(objectMapper.writeValueAsString(createCategoryCommand)));

        resultActions.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo("Java")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.parentCategory", Matchers.nullValue()));
    }
}