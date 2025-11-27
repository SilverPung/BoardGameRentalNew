package dev.apibaras.boardgamerental.service;


import dev.apibaras.boardgamerental.exception.UniqueForeignKeyException;
import dev.apibaras.boardgamerental.model.Event;
import dev.apibaras.boardgamerental.model.Overseer;
import dev.apibaras.boardgamerental.model.OverseerEvent;
import dev.apibaras.boardgamerental.model.request.OverseerEventRequest;
import dev.apibaras.boardgamerental.model.types.PermissionsType;
import dev.apibaras.boardgamerental.repository.EventRepository;
import dev.apibaras.boardgamerental.repository.OverseerEventRepository;
import dev.apibaras.boardgamerental.repository.OverseerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OverseerEventService {

    private final OverseerRepository overseerRepository;
    private final EventRepository eventRepository;
    private final OverseerEventRepository overseerEventRepository;

    @Autowired
    public OverseerEventService(OverseerRepository overseerRepository, EventRepository eventRepository, OverseerEventRepository overseerEventRepository) {
        this.overseerRepository = overseerRepository;
        this.eventRepository = eventRepository;
        this.overseerEventRepository = overseerEventRepository;
    }

    public OverseerEvent save(OverseerEventRequest overseerEventRequest) {
        OverseerEvent overseerEvent = new OverseerEvent();
        return saveOrUpdateOverseerEvent(overseerEventRequest, overseerEvent);
    }

    public void delete(Long id) {
        if (!overseerEventRepository.existsById(id)) {
            throw new EntityNotFoundException("OverseerEvent with id " + id + " not found");
        }
        overseerEventRepository.deleteById(id);
    }

    public OverseerEvent update(Long id, OverseerEventRequest overseerEventRequest) {
        OverseerEvent overseerEvent = overseerEventRepository.getValidOverseerEventById(id);
        return saveOrUpdateOverseerEvent(overseerEventRequest, overseerEvent);
    }

    private OverseerEvent saveOrUpdateOverseerEvent(OverseerEventRequest overseerEventRequest, OverseerEvent overseerEvent) {

        overseerEvent.setOverseer(
                findOverseerById(
                        overseerEventRequest.getOverseerId()
                ));

        overseerEvent.setEvent(
                findEventById(
                        overseerEventRequest.getEventId()
                ));

        overseerEvent.setPermissionsType(
                PermissionsType.valueOf(
                        overseerEventRequest.getPermissionsType()
                ));

        try {
            return overseerEventRepository.save(overseerEvent);
        } catch (DataIntegrityViolationException e) {
            handleDataIntegrityViolationException(e);
            return null; // This line will never be reached, but is required to satisfy the compiler
        }
    }

    private Overseer findOverseerById(Long overseerId) {
        return overseerRepository.getValidOverseerById(overseerId);
    }

    private Event findEventById(Long eventId) {
        return eventRepository.getValidEventById(eventId);
    }

    private void handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        if (e.getMessage().contains("Unique index or primary key violation")) {
            throw new UniqueForeignKeyException("OverseerEvent already exists");
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Data integrity violation");
        }
    }
}