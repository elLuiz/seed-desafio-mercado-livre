package br.com.ecommerce.application.product;

import br.com.ecommerce.application.common.WithMockJwt;
import br.com.ecommerce.application.product.request.ReviewProductRequest;
import br.com.ecommerce.application.product.response.ProductCreatedResponse;
import br.com.ecommerce.application.util.IntegrationTest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.stream.Stream;

@IntegrationTest
@WithMockJwt(roles = {"CREATE_PRODUCT"})
class ProductReviewControllerTest extends ProductControllerRequestSender {
    @ParameterizedTest
    @MethodSource("provideReviewWithInvalidInput")
    void shouldReturnBadRequestWhenReviewInputIsInvalid(ReviewProductRequest reviewProductRequest, List<String> expectedCodes) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/{id}/reviews",  1L)
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en-US")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewProductRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code", Matchers.containsInAnyOrder(expectedCodes.toArray())));
    }

    static Stream<Arguments> provideReviewWithInvalidInput() {
        return Stream.of(
                Arguments.of(new ReviewProductRequest(null, null, null), List.of("rating.must.not.be.null", "title.must.not.be.empty", "description.must.not.be.empty")),
                Arguments.of(new ReviewProductRequest(-1, "", ""), List.of("rating.minimum.must.not.be.lesser.than.one", "title.must.not.be.empty", "description.must.not.be.empty")),
                Arguments.of(new ReviewProductRequest(6, "A".repeat(256), "A".repeat(501)), List.of("rating.maximum.must.not.be.greater.than.five", "title.must.not.exceed.255.characters", "description.must.not.exceed.500.characters"))
        );
    }

    @Test
    @WithMockJwt(roles = {"CREATE_PRODUCT"}, subject = "35f5afb1-c754-4038-9631-b04075480b5c")
    void shouldAddReviewToProduct() throws Exception {
        ProductCreatedResponse productCreatedResponse = createProduct();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/{id}/reviews", productCreatedResponse.productId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en-US")
                .content(objectMapper.writeValueAsString(new ReviewProductRequest(4, "GOOD, aber TEUER", "The product is really outstanding, but the price is away above market"))))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.notNullValue()));
    }

    @Test
    void shouldNotAddReviewToProductWhenProductsOwnerIsTheAuthor() throws Exception {
        ProductCreatedResponse productCreatedResponse = createProduct();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/{id}/reviews", productCreatedResponse.productId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en-US")
                        .content(objectMapper.writeValueAsString(new ReviewProductRequest(4, "GOOD, aber TEUER", "The product is really outstanding, but the price is away above market"))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code", Matchers.containsInAnyOrder("owner.cannot.review.product")));
    }
}