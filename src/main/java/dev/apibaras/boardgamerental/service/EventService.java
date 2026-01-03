package dev.apibaras.boardgamerental.service;



import dev.apibaras.boardgamerental.model.event.Event;
import dev.apibaras.boardgamerental.model.event.EventRequest;
import dev.apibaras.boardgamerental.model.event.EventResponse;
import dev.apibaras.boardgamerental.model.logon.Overseer;
import dev.apibaras.boardgamerental.repository.EventRepository;
import dev.apibaras.boardgamerental.repository.OverseerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final OverseerRepository overseerRepository;


    @Autowired
    public EventService(EventRepository eventRepository, OverseerRepository overseerRepository) {
        this.overseerRepository = overseerRepository;
        this.eventRepository = eventRepository;
    }


    public Page<EventResponse> getAll(String username, int page, int size) {

        Overseer overseer = overseerRepository.getValidOverseerByUsername(username);

        return eventRepository.findByOverseersId(overseer.getId(),
                org.springframework.data.domain.PageRequest.of(page, size))
                .map(EventResponse::new);
    }

    public EventResponse getById(Long id) {
        return new EventResponse(eventRepository.getValidEventById(id));
    }


    public EventResponse save(String username, EventRequest event, Long eventId) {
        if (eventId != null) {
            log.info("Updating event id: {} by user: {}", eventId, username);
            Event existingEvent = eventRepository.getValidEventById(eventId);
            existingEvent.setName(event.getName());
            existingEvent.setDescription(event.getDescription());
            Event updatedEvent = eventRepository.save(existingEvent);
            return new EventResponse(updatedEvent);
        } else {
            log.info("Creating new event by user: {}", username);
            Overseer overseer = overseerRepository.getValidOverseerByUsername(username);
            Event newEvent = new Event();
            newEvent.setName(event.getName());
            newEvent.setDescription(event.getDescription());
            newEvent.getOverseers().add(overseer);
            Event savedEvent = eventRepository.save(newEvent);
            return new EventResponse(savedEvent);
        }

    }

    public void delete(Long id) {
        if(!eventRepository.existsById(id)){
            throw new EntityNotFoundException("Event on id " + id + " not found");
        }
        eventRepository.deleteById(id);
    }

}
