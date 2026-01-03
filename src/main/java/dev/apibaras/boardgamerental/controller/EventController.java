package dev.apibaras.boardgamerental.controller;


import dev.apibaras.boardgamerental.model.Event;

import dev.apibaras.boardgamerental.model.dto.EventRequest;
import dev.apibaras.boardgamerental.service.EventService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/event")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }



    @GetMapping("")
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAll(GetUsernameInfo()));
    }

    @PostMapping("")
    public ResponseEntity<Event> saveEvent(@RequestBody @Valid EventRequest eventRequest) {

        log.info("Saving event: {}", eventRequest);
        String username = GetUsernameInfo();

        Event event = new Event();
        event.setName(eventRequest.getName());
        event.setDescription(eventRequest.getDescription());
        event.setDate(new Date());

        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.save(username,event));
    }

    @PreAuthorize("@AuthenticationService.hasAccessToEvent(#eventId) or hasRole('ADMIN')")
    @GetMapping("/{eventId}")
    public ResponseEntity<Event> getEventById(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getById(eventId));
    }


    @PreAuthorize("@AuthenticationService.hasAccessToEvent(#eventId) or hasRole('ADMIN')")
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        eventService.delete(eventId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("@AuthenticationService.hasAccessToEvent(#eventId) or hasRole('ADMIN')")
    @PutMapping("/{eventId}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long eventId, @RequestBody Event event) { //TODO: change
        event.setId(eventId);
        return ResponseEntity.ok(eventService.save(null,event));
    }

    private String GetUsernameInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new SecurityException("Unauthenticated user cannot access username");
        }
        return auth.getName();
    }


}
