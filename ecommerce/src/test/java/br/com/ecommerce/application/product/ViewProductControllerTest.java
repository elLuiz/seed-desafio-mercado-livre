package br.com.ecommerce.application.product;

import br.com.ecommerce.application.product.response.ProductCreatedResponse;
import br.com.ecommerce.application.util.IntegrationTest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@IntegrationTest
class ViewProductControllerTest extends ProductControllerRequestSender {
    @Test
    void shouldReturnNotFoundWhenProductDoesNotExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/{id}", 1_930_299L))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void shouldReturnProductDetails() throws Exception {
        ProductCreatedResponse productCreatedResponse = createProduct();
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/{id}", productCreatedResponse.productId())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.mediaLinks", Matchers.iterableWithSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo("Nike")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", Matchers.equalTo(100.00)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.characteristics", Matchers.iterableWithSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.equalTo("Ein Schuhe")));
    }
}