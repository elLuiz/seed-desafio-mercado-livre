package br.com.ecommerce.domain.model.product.events;

import br.com.ecommerce.domain.model.common.DomainEvent;
import lombok.Getter;

import java.util.Objects;

@Getter
public class QuestionCreatedEvent extends DomainEvent {
    private final Long id;
    private final String ownerEmail;
    private final String question;

    public QuestionCreatedEvent(Long id, String ownerEmail, String question) {
        this.id = id;
        this.ownerEmail = ownerEmail;
        this.question = question;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        QuestionCreatedEvent that = (QuestionCreatedEvent) o;
        return Objects.equals(id, that.id) && Objects.equals(ownerEmail, that.ownerEmail) && Objects.equals(question, that.question);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, ownerEmail, question);
    }
}