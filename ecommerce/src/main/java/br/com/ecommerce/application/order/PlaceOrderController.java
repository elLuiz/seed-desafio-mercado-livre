package br.com.ecommerce.application.order;

import br.com.ecommerce.application.order.request.OrderRequest;
import br.com.ecommerce.domain.model.order.OrderDetails;
import br.com.ecommerce.domain.model.order.command.CreateOrderCommand;
import br.com.ecommerce.service.order.PlaceOrderService;
import br.com.ecommerce.service.common.SessionUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
public class PlaceOrderController {
    private final PlaceOrderService placeOrderService;
    private final SessionUserService sessionUserService;

    public PlaceOrderController(PlaceOrderService placeOrderService, SessionUserService sessionUserService) {
        this.placeOrderService = placeOrderService;
        this.sessionUserService = sessionUserService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("!@productPermissionValidator.isOwnerOf(#orderRequest.productId, #jwt)")
    public ResponseEntity<Object> order(@AuthenticationPrincipal Jwt jwt,
                                        @RequestBody @Valid OrderRequest orderRequest) {
        OrderDetails orderDetails = placeOrderService.order(new CreateOrderCommand(sessionUserService.loadUserBySubject(jwt.getSubject()), orderRequest.productId(), orderRequest.paymentGateway(), orderRequest.quantity()));
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                .header(HttpHeaders.LOCATION, orderDetails.redirectURI())
                .build();
    }
}