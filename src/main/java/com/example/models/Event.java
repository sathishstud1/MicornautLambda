package com.example.models;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Introspected
public class Event {

    @NotNull
    private EventType eventType;

    @NotBlank
    private String eventSubType;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDate eventDate;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDate recordDate;

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getEventSubType() {
        return eventSubType;
    }

    public void setEventSubType(String eventSubType) {
        this.eventSubType = eventSubType;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }

    @Override
    public String toString() {
        return String.format("Event(eventType=%s, eventSubType=%s, eventDate=%s, recordDate=%s)",
                eventType, eventSubType, eventDate, recordDate);
    }
}
