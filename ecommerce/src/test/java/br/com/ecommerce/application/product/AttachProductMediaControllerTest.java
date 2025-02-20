package br.com.ecommerce.application.product;

import br.com.ecommerce.application.RequestSender;
import br.com.ecommerce.application.common.WithMockJwt;
import br.com.ecommerce.application.product.response.ProductCreatedResponse;
import br.com.ecommerce.application.util.IntegrationTest;
import br.com.ecommerce.commons.MultipartFactory;
import br.com.ecommerce.domain.model.product.command.ProductCharacteristics;
import br.com.ecommerce.domain.model.product.command.RegisterProductCommand;
import br.com.ecommerce.service.category.CategoryRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

@IntegrationTest
@Sql(scripts = {"/insert-categories.sql", "/insert-users.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@WithMockJwt(roles = {"CREATE_PRODUCT"})
class AttachProductMediaControllerTest extends RequestSender {
    @MockitoBean
    ProductPermissionValidator productPermissionValidator;
    @Autowired
    CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        Mockito.when(productPermissionValidator.isOwnerOf(Mockito.anyLong(), Mockito.any()))
                .thenReturn(true);
    }

    @Test
    void shouldReturnErrorWhenThereAreMoreFilesThanTheAllowed() throws Exception {
        MockMultipartFile file = MultipartFactory.getMultipartFile("img.png", "src/test/resources/assets/img.png");
        MockMultipartFile file1 = MultipartFactory.getMultipartFile("img1.png", "src/test/resources/assets/img_1.png");
        MockMultipartFile file2 = MultipartFactory.getMultipartFile("img2.png", "src/test/resources/assets/img_2.png");
        MockMultipartFile file3 = MultipartFactory.getMultipartFile("img3.png", "src/test/resources/assets/img_3.png");
        MockMultipartFile file4 = MultipartFactory.getMultipartFile("img4.png", "src/test/resources/assets/img_4.png");
        MockMultipartFile file5 = MultipartFactory.getMultipartFile("img4.png", "src/test/resources/assets/img_5.png");

        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PUT, "/api/v1/products/{productId}/attachments/medias", 1)
                .file(file)
                .file(file1)
                .file(file2)
                .file(file3)
                .file(file4)
                .file(file5)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en-US")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code", Matchers.containsInAnyOrder("files.must.not.exceed.limit")));
    }

    @Test
    void shouldReturnErrorWhenAtLeastOneFileIsNotCompliantToExpectedFormats() throws Exception {
        MockMultipartFile file = MultipartFactory.getMultipartFile("img.png", "src/test/resources/assets/img.png");
        MockMultipartFile file1 = MultipartFactory.getMultipartFile("img1.png", "src/test/resources/assets/img_1.png");
        MockMultipartFile file2 = MultipartFactory.getMultipartFile("img2.png", "src/test/resources/assets/img_2.png");
        MockMultipartFile file3 = MultipartFactory.getMultipartFile("img3.png", "src/test/resources/assets/img_3.png");
        MockMultipartFile file4 = MultipartFactory.getMultipartFile("delete-categories.sql", "src/test/resources/delete-categories.sql");

        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PUT, "/api/v1/products/{productId}/attachments/medias", 1)
                        .file(file)
                        .file(file1)
                        .file(file2)
                        .file(file3)
                        .file(file4)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en-US")
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code", Matchers.containsInAnyOrder("invalid.media.format")));
    }

    @Test
    void shouldReturnErrorWhenFileExceedLimit() throws Exception {
        MockMultipartFile file = MultipartFactory.getMultipartFile("fireworks.png", "src/test/resources/assets/pexels-jibarofoto-1738641.jpg");
        MockMultipartFile file1 = MultipartFactory.getMultipartFile("fireworks1.png", "src/test/resources/assets/pexels-jibarofoto-1738641.jpg");
        MockMultipartFile file2 = MultipartFactory.getMultipartFile("fireworks1.png", "src/test/resources/assets/pexels-jibarofoto-1738641.jpg");
        MockMultipartFile file3 = MultipartFactory.getMultipartFile("fireworks1.png", "src/test/resources/assets/pexels-jibarofoto-1738641.jpg");
        MockMultipartFile file4 = MultipartFactory.getMultipartFile("fireworks1.png", "src/test/resources/assets/pexels-jibarofoto-1738641.jpg");

        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PUT, "/api/v1/products/{productId}/attachments/medias", 1)
                        .file(file)
                        .file(file1)
                        .file(file2)
                        .file(file3)
                        .file(file4)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en-US"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].code", Matchers.containsInAnyOrder("file.exceeds.max.size", "file.exceeds.max.size", "request.exceeds.limit")));
    }

    @Test
    @WithMockJwt(roles = {"CREATE_PRODUCT"}, subject = "25a5afb1-c754-4038-9631-b04075480b5c")
    void shouldReturn204WhenImagesHaveBeenSuccessfullyAttachedToProduct() throws Exception {
        MockMultipartFile file = MultipartFactory.getMultipartFile("img.png", "src/test/resources/assets/img.png");
        MockMultipartFile file1 = MultipartFactory.getMultipartFile("img1.png", "src/test/resources/assets/img_1.png");
        MockMultipartFile file2 = MultipartFactory.getMultipartFile("img2.png", "src/test/resources/assets/img_2.png");
        MockMultipartFile file3 = MultipartFactory.getMultipartFile("img3.png", "src/test/resources/assets/img_3.png");

        RegisterProductCommand registerProductCommand = new RegisterProductCommand("Nike", BigDecimal.valueOf(100), 10, getCharacteristics(new ProductCharacteristics("Size", "44")), "Ein Schuhe", categoryRepository.findByName("Programming").orElseThrow().getId());
        byte[] response = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en-US")
                .content(objectMapper.writeValueAsBytes(registerProductCommand)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
        ProductCreatedResponse productCreatedResponse = objectMapper.readValue(response, ProductCreatedResponse.class);

        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PUT, "/api/v1/products/{productId}/attachments/medias", productCreatedResponse.productId())
                .file(file)
                .file(file1)
                .file(file2)
                .file(file3)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en-US"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    static List<ProductCharacteristics> getCharacteristics(ProductCharacteristics ...productCharacteristics) {
        return Stream.of(productCharacteristics).toList();
    }
}