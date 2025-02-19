package br.com.ecommerce.service.common;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface Storage {
    List<StorageFile> upload(MultipartFile[] files);
}