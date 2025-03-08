package br.com.ecommerce.domain.model.product.command;

import br.com.ecommerce.domain.model.session.SessionUser;

public record RegisterQuestionCommand(SessionUser user, Long productId, String question) {
}
