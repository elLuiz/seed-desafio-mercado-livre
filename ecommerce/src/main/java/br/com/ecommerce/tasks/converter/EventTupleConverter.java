package br.com.ecommerce.tasks.converter;

import br.com.ecommerce.tasks.model.Event;
import jakarta.persistence.Tuple;

import java.util.Collections;
import java.util.List;

public class EventTupleConverter {
    private EventTupleConverter() {}

    public static List<Event> convert(List<Tuple> tuples) {
        if (tuples == null || tuples.isEmpty()) {
            return Collections.emptyList();
        }
        return tuples.stream()
                .map(tuple -> new Event(tuple.get(0, Long.class), tuple.get(1, String.class), tuple.get(2, String.class), tuple.get(3, String.class)))
                .toList();
    }
}