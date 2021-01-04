package com.example;

import com.example.models.Event;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class InMemoryDatastore {
    private Map<String, Event> datastore = new HashMap<>();

    public Map<String, Event> getDatastore() {
        return datastore;
    }
}
