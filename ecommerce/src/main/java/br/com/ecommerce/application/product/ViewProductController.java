package br.com.ecommerce.application.product;

import br.com.ecommerce.application.product.response.ProductResponse;
import br.com.ecommerce.application.product.service.ViewProductService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
public class ViewProductController {
    private final ViewProductService viewProductService;

    public ViewProductController(ViewProductService viewProductService) {
        this.viewProductService = viewProductService;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponse> getProductById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(viewProductService.findById(id));
    }
}