package br.com.ecommerce.application.common;

import lombok.experimental.UtilityClass;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@UtilityClass
public class ResourceCreatedResponse {
    public static <T> ResponseEntity<T> created(T object, String path, Object ...params) {
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                .path(path)
                .buildAndExpand(params)
                .toUri())
                .body(object);
    }
}