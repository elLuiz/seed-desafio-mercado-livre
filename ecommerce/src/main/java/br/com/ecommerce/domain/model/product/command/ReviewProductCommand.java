package br.com.ecommerce.domain.model.product.command;

import br.com.ecommerce.domain.model.session.SessionUser;

public record ReviewProductCommand(Long productId, Integer rating, String title, String description, SessionUser author) {
}
