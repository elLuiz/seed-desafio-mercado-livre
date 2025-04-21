package br.com.ecommerce.application.order;

import br.com.ecommerce.application.common.WithMockJwt;
import br.com.ecommerce.application.order.request.OrderRequest;
import br.com.ecommerce.application.product.ProductControllerRequestSender;
import br.com.ecommerce.application.product.response.ProductCreatedResponse;
import br.com.ecommerce.application.util.IntegrationTest;
import br.com.ecommerce.domain.model.order.PaymentGateway;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.stream.Stream;

@IntegrationTest
class PlaceOrderControllerTest extends ProductControllerRequestSender {
    @BeforeEach
    void setUp() {
        System.setProperty("PAG_SEGURO_REDIRECT_URL", "http://junit.pagseguro.br");
        System.setProperty("PAYPAL_REDIRECT_URL", "http://junit.paypal.br");
        System.setProperty("CALLBACK_URL", "https://my-ecommerce");
    }

    @ParameterizedTest
    @WithMockJwt(subject = "35f5afb1-c754-4038-9631-b04075480b5c")
    @MethodSource("provideInvalidOrderParameters")
    public void shouldReturnBadRequestWhenOrderIsInvalid(OrderRequest orderRequest, List<String> expectedCodes) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/orders")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(orderRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code", Matchers.containsInAnyOrder(expectedCodes.toArray())));
    }

    static Stream<Arguments> provideInvalidOrderParameters() {
        return Stream.of(
                Arguments.of(new OrderRequest(null, null, null), List.of("product.must.not.be.null", "payment.gateway.must.be.selected", "quantity.must.not.be.null")),
                Arguments.of(new OrderRequest(10L, PaymentGateway.PAYPAL, 0), List.of("quantity.must.be.positive")),
                Arguments.of(new OrderRequest(10L, PaymentGateway.PAYPAL, -1), List.of("quantity.must.be.positive"))

        );
    }

    @Test
    @WithMockJwt
    void shouldRejectPlacingOrderForSameTheOwner() throws Exception {
        ProductCreatedResponse productCreatedResponse = createProduct();
        OrderRequest orderRequest = new OrderRequest(productCreatedResponse.productId(), PaymentGateway.PAYPAL, 11);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/orders")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockJwt(subject = "35f5afb1-c754-4038-9631-b04075480b5c")
    void shouldNotPlaceOrderWhenThereIsNoStockAvailable() throws Exception {
        ProductCreatedResponse productCreatedResponse = createProduct();
        OrderRequest orderRequest = new OrderRequest(productCreatedResponse.productId(), PaymentGateway.PAYPAL, 11);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/orders")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code", Matchers.contains("product.out.of.stock")));
    }

    @ParameterizedTest
    @EnumSource(value = PaymentGateway.class, names = {"PAYPAL", "PAG_SEGURO"})
    @WithMockJwt(subject = "35f5afb1-c754-4038-9631-b04075480b5c")
    void shouldRegisterOrder(PaymentGateway paymentGateway) throws Exception {
        ProductCreatedResponse productCreatedResponse = createProduct();
        OrderRequest orderRequest = new OrderRequest(productCreatedResponse.productId(), paymentGateway, 2);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/orders")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isMovedPermanently())
                .andExpect(MockMvcResultMatchers.header().exists("Location"));
    }
}