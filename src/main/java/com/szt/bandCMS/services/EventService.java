package com.szt.bandCMS.services;

import com.szt.bandCMS.models.Event;
import com.szt.bandCMS.repositories.EventRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getEvents() {
        return eventRepository.findAllByOrderByStartDateDesc();
    }
    public List<Event> getEvents(int count) {
        return eventRepository.findAllByOrderByStartDateDesc()
                .stream()
                .limit(count)
                .collect(Collectors.toList());
    }

    public List<Event> getEventsVisible() {
        return eventRepository.findAllByOrderByStartDateDesc()
                .stream()
                .filter(event -> !event.getStatus().equals("Hidden"))
                .collect(Collectors.toList());
    }
    public List<Event> getEventsVisible(int count) {
        return eventRepository.findAllByOrderByStartDateDesc()
                .stream()
                .filter(event -> !event.getStatus().equals("Hidden"))
                .limit(count)
                .collect(Collectors.toList());
    }

    public Event updateStatus(Event event, String status) {
        event.setStatus(status);
        return event;
    }
    public List<String> getAvailableStatuses(String currentStatus) {
        switch (currentStatus) {
            case "Hidden":
                return List.of("Future", "Past");
            case "Future":
                return List.of("Hidden", "In progress", "Cancelled");
            case "In progress":
                return List.of("Past");
            case "Past":
                return List.of("Hidden");
            case "Cancelled":
                return List.of();
            default:
                return Arrays.asList();
        }
    }
}
