package com.szt.bandCMS.services;

import com.szt.bandCMS.models.Event;
import com.szt.bandCMS.repositories.EventRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

    public Optional<Event> getEventById(long id) {
        return eventRepository.findById(id);
    }

    public Event updateStatus(Event event, String status) {
        event.setStatus(status);
        return event;
    }

    public List<String> getAvailableStatuses(String currentStatus) {
        switch (currentStatus) {
            case "Hidden":
                return List.of("Hidden", "Future", "Past");
            case "Future":
                return List.of("Future", "Hidden", "In progress", "Cancelled");
            case "In progress":
                return List.of("In progress", "Past");
            case "Past":
                return List.of("Past", "Hidden");
            case "Cancelled":
                return List.of("Cancelled", "Cancelled");
            default:
                return Arrays.asList("Hidden", "Future", "In progress", "Past", "Cancelled");
        }
    }

    public void save(Event event) {
        eventRepository.save(event);
    }

    public void delete(long id) {
        eventRepository.deleteById(id);
    }
}
