package dev.apibaras.boardgamerental.controller;



import dev.apibaras.boardgamerental.model.OverseerEvent;
import dev.apibaras.boardgamerental.model.request.OverseerEventRequest;
import dev.apibaras.boardgamerental.service.OverseerEventService;
import jakarta.persistence.UniqueConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/overseer/event")

public class OverseerEventController {


    private final OverseerEventService overseerEventService;

    @Autowired
    public OverseerEventController(OverseerEventService overseerEventService) {
        this.overseerEventService = overseerEventService;
    }

    @PostMapping
    public ResponseEntity<OverseerEvent> save(@RequestBody OverseerEventRequest overseerEventRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(overseerEventService.save(overseerEventRequest));
    }

    @PutMapping("{id}")
    public ResponseEntity<OverseerEvent> update(@PathVariable Long id, @RequestBody OverseerEventRequest overseerEventRequest){
        return ResponseEntity.ok(overseerEventService.update(id, overseerEventRequest));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        overseerEventService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
