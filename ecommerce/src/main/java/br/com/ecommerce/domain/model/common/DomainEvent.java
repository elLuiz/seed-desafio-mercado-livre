package br.com.ecommerce.domain.model.common;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.UUID;

@Getter
public abstract class DomainEvent extends ApplicationEvent {
    private String eventId;
    private OffsetDateTime occurredAt;

    protected DomainEvent() {
        super("Domain Event");
        this.eventId = UUID.randomUUID().toString();
        this.occurredAt = OffsetDateTime.now(ZoneId.of("UTC"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DomainEvent that = (DomainEvent) o;
        return Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(eventId);
    }
}