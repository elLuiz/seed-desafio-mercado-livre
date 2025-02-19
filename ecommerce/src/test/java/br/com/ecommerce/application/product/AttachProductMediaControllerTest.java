package br.com.ecommerce.application.product;

import br.com.ecommerce.application.RequestSender;
import br.com.ecommerce.application.common.WithMockJwt;
import br.com.ecommerce.application.util.IntegrationTest;
import br.com.ecommerce.commons.MultipartFactory;
import org.hamcrest.Matchers;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@IntegrationTest
@WithMockJwt(roles = {"CREATE_PRODUCT"})
class AttachProductMediaControllerTest extends RequestSender {
    @MockitoBean
    ProductPermissionValidator productPermissionValidator;

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
    void shouldReturn204WhenImagesHaveBeenSuccessfullyAttachedToProduct() throws Exception {
        MockMultipartFile file = MultipartFactory.getMultipartFile("img.png", "src/test/resources/assets/img.png");
        MockMultipartFile file1 = MultipartFactory.getMultipartFile("img1.png", "src/test/resources/assets/img_1.png");
        MockMultipartFile file2 = MultipartFactory.getMultipartFile("img2.png", "src/test/resources/assets/img_2.png");
        MockMultipartFile file3 = MultipartFactory.getMultipartFile("img3.png", "src/test/resources/assets/img_3.png");

        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PUT, "/api/v1/products/{productId}/attachments/medias", 1)
                .file(file)
                .file(file1)
                .file(file2)
                .file(file3)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en-US"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}