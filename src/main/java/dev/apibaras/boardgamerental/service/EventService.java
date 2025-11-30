package dev.apibaras.boardgamerental.service;



import dev.apibaras.boardgamerental.model.Event;
import dev.apibaras.boardgamerental.model.Overseer;
import dev.apibaras.boardgamerental.repository.EventRepository;
import dev.apibaras.boardgamerental.repository.OverseerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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


    public List<Event> getAll(String username) {

        Overseer overseer = overseerRepository.getValidOverseerByUsername(username);

        return eventRepository.findByOverseersId(overseer.getId());
    }

    public Event getById(Long id) {
        return eventRepository.getValidEventById(id);
    }


    public Event save(String username,Event event) {

        if (username != null) {
            Overseer overseer = overseerRepository.getValidOverseerByUsername(username);
            event.addOverseer(overseer);
        }
        log.debug("save event: {}", event);
        return eventRepository.save(event);
    }

    public void delete(Long id) {
        if(!eventRepository.existsById(id)){
            throw new EntityNotFoundException("Event on id " + id + " not found");
        }
        eventRepository.deleteById(id);
    }

}
