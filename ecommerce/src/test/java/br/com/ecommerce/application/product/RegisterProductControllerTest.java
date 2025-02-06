package br.com.ecommerce.application.product;

import br.com.ecommerce.application.RequestSender;
import br.com.ecommerce.application.common.WithMockJwt;
import br.com.ecommerce.application.util.IntegrationTest;
import br.com.ecommerce.domain.model.product.command.ProductCharacteristics;
import br.com.ecommerce.domain.model.product.command.RegisterProductCommand;
import br.com.ecommerce.service.category.CategoryRepository;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

@IntegrationTest
@Sql(scripts = {"/insert-categories.sql", "/insert-users.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class RegisterProductControllerTest extends RequestSender {
    @Autowired
    CategoryRepository categoryRepository;

    @ParameterizedTest
    @MethodSource("provideInvalidProductsAndExpectedResponses")
    @WithMockJwt(roles = {"CREATE_CATEGORY", "CREATE_PRODUCT"})
    void shouldReturnBadRequestWhenProductIsInvalid(RegisterProductCommand registerProductCommand, List<String> expectedErrors) throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en-US")
                .content(objectMapper.writeValueAsBytes(registerProductCommand)));

        resultActions
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code", Matchers.containsInAnyOrder(expectedErrors.toArray())))
                .andDo(MockMvcResultHandlers.print());
    }

    static Stream<Arguments> provideInvalidProductsAndExpectedResponses() {
        return Stream.of(
                Arguments.of(new RegisterProductCommand(null, null, null, null, null, null), getExpectedErrors("product.name.must.not.be.empty", "price.must.not.be.null", "quantity.must.not.be.empty", "characteristics.must.be.specified", "description.must.not.be.empty", "category.must.not.be.null")),
                Arguments.of(new RegisterProductCommand("", BigDecimal.valueOf(1.99), 0, null, "", 1L), getExpectedErrors("product.name.must.not.be.empty", "invalid.price", "quantity.must.be.positive", "characteristics.must.be.specified", "description.must.not.be.empty")),
                Arguments.of(new RegisterProductCommand("A".repeat(256), BigDecimal.valueOf(2.00), -1, getCharacteristics(), "A".repeat(1_001), 1L), getExpectedErrors("product.name.must.not.surpass.255.characters", "quantity.must.be.positive", "characteristics.must.be.specified", "description.must.not.surpass.1000.characters")),
                Arguments.of(new RegisterProductCommand("Product ABC - Test", BigDecimal.valueOf(30_000.50), 10, getCharacteristics(new ProductCharacteristics("", ""), new ProductCharacteristics("a".repeat(101), "b".repeat(102))), "#### Product Description \n Test", 10L), getExpectedErrors("property.must.not.be.empty", "property.value.must.not.be.empty", "property.must.not.surpass.100.characters", "property.value.must.not.surpass.100.characters"))
        );
    }

    static List<ProductCharacteristics> getCharacteristics(ProductCharacteristics ...productCharacteristics) {
        return Stream.of(productCharacteristics).toList();
    }

    @Test
    @WithMockJwt(roles = {"CREATE_CATEGORY"})
    void shouldReturnForbiddenWhenUserDoesHavePermissionToRegisterProducts() throws Exception {
        RegisterProductCommand registerProductCommand = new RegisterProductCommand("Nike", BigDecimal.valueOf(100), 10, getCharacteristics(new ProductCharacteristics("Size", "44")), "Ein Schuhe", 1L);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en-US")
                .content(objectMapper.writeValueAsBytes(registerProductCommand)));

        resultActions.andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    // The subject can be found in the script insert-users.sql
    @WithMockJwt(roles = {"CREATE_PRODUCT"}, subject = "25a5afb1-c754-4038-9631-b04075480b5c")
    void shouldRegisterProduct() throws Exception {
        RegisterProductCommand registerProductCommand = new RegisterProductCommand("Nike", BigDecimal.valueOf(100), 10, getCharacteristics(new ProductCharacteristics("Size", "44")), "Ein Schuhe", categoryRepository.findByName("Programming").orElseThrow().getId());
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en-US")
                .content(objectMapper.writeValueAsBytes(registerProductCommand)));

        resultActions.andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcResultHandlers.print());
    }
}