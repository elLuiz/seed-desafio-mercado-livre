package br.com.ecommerce.domain.model.common;

import lombok.Getter;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.UUID;

@Getter
public abstract class DomainEvent<I> {
    private I id;
    private String eventId;
    private OffsetDateTime occurredAt;
    private String type;
    private String description;

    protected DomainEvent(I id, String type, String description) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.eventId = UUID.randomUUID().toString();
        this.occurredAt = OffsetDateTime.now(ZoneId.of("UTC"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DomainEvent<?> that = (DomainEvent<?>) o;
        return Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(eventId);
    }
}