package br.com.ecommerce.domain.model.product.events;

import br.com.ecommerce.domain.model.common.DomainEvent;
import br.com.ecommerce.domain.model.product.ProductQuestion;
import lombok.Getter;

import java.util.Objects;

@Getter
public class QuestionCreatedEvent extends DomainEvent<Long> {
    private final String ownerEmail;
    private final String question;

    public QuestionCreatedEvent(Long id, String ownerEmail, String question) {
        super(id, ProductQuestion.class.getSimpleName(), "product.question.created");
        this.ownerEmail = ownerEmail;
        this.question = question;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        QuestionCreatedEvent that = (QuestionCreatedEvent) o;
        return Objects.equals(ownerEmail, that.ownerEmail) && Objects.equals(question, that.question);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), ownerEmail, question);
    }
}