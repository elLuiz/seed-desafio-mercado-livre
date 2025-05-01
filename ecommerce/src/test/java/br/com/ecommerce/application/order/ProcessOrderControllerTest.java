package br.com.ecommerce.application.order;

import br.com.ecommerce.application.common.WithMockJwt;
import br.com.ecommerce.application.order.request.OrderRequest;
import br.com.ecommerce.application.product.ProductControllerRequestSender;
import br.com.ecommerce.application.product.response.ProductCreatedResponse;
import br.com.ecommerce.application.util.IntegrationTest;
import br.com.ecommerce.domain.model.order.Order;
import br.com.ecommerce.domain.model.order.OrderStatus;
import br.com.ecommerce.domain.model.order.PaymentGateway;
import br.com.ecommerce.service.order.OrderRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

@IntegrationTest
class ProcessOrderControllerTest extends ProductControllerRequestSender {
    @Autowired
    OrderRepository orderRepository;

    @Test
    @WithMockJwt(subject = "35f5afb1-c754-4038-9631-b04075480b5c")
    void shouldProcessOrder() throws Exception {
        ProductCreatedResponse productCreatedResponse = createProduct();
        OrderRequest orderRequest = new OrderRequest(productCreatedResponse.productId(), PaymentGateway.PAYPAL, 2);
        String orderId = placeOrder(orderRequest);
        String orderTransactionId = UUID.randomUUID().toString();
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/orders/{orderId}", orderId)
                        .queryParam("transactionId", orderTransactionId)
                        .queryParam("status", "1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        
        Assertions.assertEquals(OrderStatus.PROCESSED, orderRepository.findByPurchaseId(orderId)
                .map(Order::getOrderStatus)
                .orElse(null));
    }

    @Test
    @WithMockJwt(subject = "35f5afb1-c754-4038-9631-b04075480b5c")
    void shouldNotReprocessOrder() throws Exception {
        ProductCreatedResponse productCreatedResponse = createProduct();
        OrderRequest orderRequest = new OrderRequest(productCreatedResponse.productId(), PaymentGateway.PAYPAL, 2);
        String orderId = placeOrder(orderRequest);
        String orderTransactionId = UUID.randomUUID().toString();
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/orders/{orderId}", orderId)
                        .queryParam("transactionId", orderTransactionId)
                        .queryParam("status", "1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/orders/{orderId}", orderId)
                .queryParam("transactionId", orderTransactionId)
                .queryParam("status", "0")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code", Matchers.contains("order.already.processed")));

        Assertions.assertEquals(OrderStatus.PROCESSED, orderRepository.findByPurchaseId(orderId)
                .map(Order::getOrderStatus)
                .orElse(null));
    }

    private String placeOrder(OrderRequest orderRequest) throws Exception {
        String processingOrderURI = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/orders")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andReturn()
                .getResponse()
                .getHeader("Location");
        String[] parts = processingOrderURI.split("/");
        return parts[parts.length - 1];
    }
}