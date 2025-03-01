package br.com.ecommerce.application.product;

import br.com.ecommerce.application.RequestSender;
import br.com.ecommerce.application.common.JwtFactory;
import br.com.ecommerce.application.common.WithMockJwt;
import br.com.ecommerce.application.product.response.ProductCreatedResponse;
import br.com.ecommerce.domain.model.product.command.ProductCharacteristics;
import br.com.ecommerce.domain.model.product.command.RegisterProductCommand;
import br.com.ecommerce.service.category.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

public abstract class ProductControllerTest extends RequestSender {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    JwtFactory jwtFactory;

    public ProductCreatedResponse createProduct() throws Exception {
        RegisterProductCommand registerProductCommand = new RegisterProductCommand("Nike", BigDecimal.valueOf(100), 10, getCharacteristics(new ProductCharacteristics("Size", "44")), "Ein Schuhe", categoryRepository.findByName("Programming").orElseThrow().getId());
        byte[] response = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en-US")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtFactory.createJwt(false, "25a5afb1-c754-4038-9631-b04075480b5c", new String[]{"CREATE_PRODUCT"}).getTokenValue()))
                        .content(objectMapper.writeValueAsBytes(registerProductCommand)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
        return objectMapper.readValue(response, ProductCreatedResponse.class);
    }

    static List<ProductCharacteristics> getCharacteristics(ProductCharacteristics ...productCharacteristics) {
        return Stream.of(productCharacteristics).toList();
    }
}