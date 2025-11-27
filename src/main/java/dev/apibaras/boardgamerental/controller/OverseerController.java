package dev.apibaras.boardgamerental.controller;



import dev.apibaras.boardgamerental.model.Overseer;
import dev.apibaras.boardgamerental.service.OverseerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/overseer")
public class OverseerController {

    private final OverseerService overseerService;

    @Autowired
    public OverseerController(OverseerService overseerService) {
        this.overseerService = overseerService;
    }


    @GetMapping("")
    public ResponseEntity<List<Overseer>> getAllOverseers() {
        return ResponseEntity.ok(overseerService.getAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Overseer> getOverseerById(@PathVariable Long id) {
        return ResponseEntity.ok(overseerService.getById(id));
    }

    @PostMapping("")
    public ResponseEntity<Overseer> createOverseer(@RequestBody Overseer overseer) {
        return ResponseEntity.ok(overseerService.save(overseer));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteOverseer(@PathVariable Long id) {
        overseerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<Overseer> updateOverseer(@PathVariable Long id, @RequestBody Overseer overseer) {

        return ResponseEntity.ok(overseerService.update(id,overseer));
    }
}
