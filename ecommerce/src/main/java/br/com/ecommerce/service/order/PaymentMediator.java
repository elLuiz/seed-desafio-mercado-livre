package br.com.ecommerce.service.order;

import br.com.ecommerce.domain.model.order.Order;
import br.com.ecommerce.domain.model.order.OrderPayment;
import br.com.ecommerce.domain.model.order.PaymentGateway;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PaymentMediator {
    private final List<Gateway> gateways;
    private final Map<PaymentGateway, Gateway> gatewayMap = new HashMap<>();

    public PaymentMediator(List<Gateway> gateways) {
        this.gateways = gateways;
    }

    @PostConstruct
    void init() {
        for (Gateway gateway : gateways) {
            gatewayMap.put(gateway.type(), gateway);
        }
    }

    public OrderPayment pay(Order order, PaymentGateway paymentGateway) {
        return gatewayMap.get(paymentGateway).process(order);
    }
}