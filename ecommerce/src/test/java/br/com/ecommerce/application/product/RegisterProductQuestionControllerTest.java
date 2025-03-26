package br.com.ecommerce.application.product;

import br.com.ecommerce.application.common.WithMockJwt;
import br.com.ecommerce.application.product.request.ProductQuestionRequest;
import br.com.ecommerce.application.product.response.ProductCreatedResponse;
import br.com.ecommerce.application.product.response.ProductQuestionCreatedResponse;
import br.com.ecommerce.application.util.IntegrationTest;
import br.com.ecommerce.infrastructure.listener.EventRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.stream.Stream;

@IntegrationTest
@WithMockJwt(roles = {"CREATE_PRODUCT"}, subject = "35f5afb1-c754-4038-9631-b04075480b5c")
class RegisterProductQuestionControllerTest extends ProductControllerRequestSender {
    @Autowired
    EventRepository eventRepository;

    @ParameterizedTest
    @MethodSource("provideInvalidQuestions")
    void shouldReturnBadRequestWhenInputIsInvalid(String question, String expectedCode) throws Exception {
        ProductCreatedResponse product = createProduct();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/{id}/questions", product.productId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en-US")
                .content(objectMapper.writeValueAsString(new ProductQuestionRequest(question))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code", Matchers.contains(expectedCode)));
    }

    static Stream<Arguments> provideInvalidQuestions() {
        return Stream.of(
                Arguments.of("", "question.must.not.be.empty"),
                Arguments.of(null, "question.must.not.be.empty"),
                Arguments.of("A".repeat(301), "question.must.not.exceed.300.characters")
        );
    }

    @Test
    @WithMockJwt(roles = {"CREATE_PRODUCT"})
    void shouldReturnBadRequestWhenOwnerIsAskingQuestion() throws Exception {
        ProductCreatedResponse product = createProduct();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/{id}/questions", product.productId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en-US")
                        .content(objectMapper.writeValueAsString(new ProductQuestionRequest("Is the product good?"))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code", Matchers.contains("owner.cannot.ask.about.own.product")));

    }

    @Test
    void shouldRegisterQuestionAboutProduct() throws Exception {
        ProductCreatedResponse product = createProduct();
        String contentAsString = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/{id}/questions", product.productId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en-US")
                        .content(objectMapper.writeValueAsString(new ProductQuestionRequest("Is the product good?"))))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.question", Matchers.equalTo("Is the product good?")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.registeredAt", Matchers.notNullValue()))
                .andReturn()
                .getResponse()
                .getContentAsString();
        ProductQuestionCreatedResponse productQuestionCreatedResponse = objectMapper.readValue(contentAsString, ProductQuestionCreatedResponse.class);

        Assertions.assertEquals(1, eventRepository.countEventsByAggregateId(productQuestionCreatedResponse.id()));
    }
}