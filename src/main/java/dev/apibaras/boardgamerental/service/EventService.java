package dev.apibaras.boardgamerental.service;



import dev.apibaras.boardgamerental.model.Event;
import dev.apibaras.boardgamerental.repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;


    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }


    public List<Event> getAll() {
        return eventRepository.findAll();
    }

    public Event getById(Long id) {
        return eventRepository.getValidEventById(id);
    }

    public Event save(Event event) {
        return eventRepository.save(event);
    }

    public void delete(Long id) {
        if(!eventRepository.existsById(id)){
            throw new EntityNotFoundException("Event on id " + id + " not found");
        }
        eventRepository.deleteById(id);
    }

    public Event update(Long id, Event event) {
        if(!eventRepository.existsById(id)){
            throw new EntityNotFoundException("Event on id " + id + " not found");
        }
        event.setId(id);
        return eventRepository.save(event);

    }
}
