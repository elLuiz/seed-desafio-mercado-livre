package br.com.ecommerce.commons;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MultipartFactory {
    private MultipartFactory() {}

    public static @NotNull MockMultipartFile getMultipartFile(String image, String path) throws IOException {
        return new MockMultipartFile("media", image, MediaType.IMAGE_PNG_VALUE, getFileInputStream(path));
    }

    static InputStream getFileInputStream(String path) throws IOException {
        return new FileInputStream(path);
    }
}
