package com.example;

import com.example.models.Event;
import com.example.models.EventSaved;
import com.example.utils.RandomString;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class EventService {
    @Inject
    private InMemoryDatastore inMemoryDatastore;

    public String save(Event event) {
        String eventId = "evt_" + RandomString.get(24);
        inMemoryDatastore.getDatastore().put(eventId, event);
        return eventId;
    }

    public List<EventSaved> getAll() {
        return inMemoryDatastore.getDatastore()
                .keySet()
                .stream()
                .map(EventSaved::new)
                .collect(Collectors.toList());
    }

    public Event get(String eventId) {
        return inMemoryDatastore.getDatastore().get(eventId);
    }

    public Event update(String eventId, Event event) {
        return inMemoryDatastore.getDatastore().replace(eventId, event);
    }
}
