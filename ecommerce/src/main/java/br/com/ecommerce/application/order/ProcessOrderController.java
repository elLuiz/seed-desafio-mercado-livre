package br.com.ecommerce.application.order;

import br.com.ecommerce.service.order.ProcessOrderService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
public class ProcessOrderController {
    private final ProcessOrderService processOrderService;

    public ProcessOrderController(ProcessOrderService processOrderService) {
        this.processOrderService = processOrderService;
    }

    @PutMapping(value = "/{orderId}",consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> processOrder(@PathVariable("orderId") String orderId,
                                          @RequestParam("transactionId") String transactionId,
                                          @RequestParam("status") String status) {
        processOrderService.processOrder(orderId, transactionId, status);
        return ResponseEntity.noContent().build();
    }
}