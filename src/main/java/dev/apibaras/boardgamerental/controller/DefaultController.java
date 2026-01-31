package dev.apibaras.boardgamerental.controller;


import dev.apibaras.boardgamerental.model.boardgame.BggThingDto;
import dev.apibaras.boardgamerental.model.boardgame.BoardGameSearchResponse;
import dev.apibaras.boardgamerental.service.BggService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/")
public class DefaultController {


    private final BggService bggService;

    @Autowired
    public DefaultController(BggService bggService) {
        this.bggService = bggService;
    }

    @GetMapping("/search-bgg/{query}")
    public ResponseEntity<Set<BoardGameSearchResponse>> searchBgg(@PathVariable String query) {
        return ResponseEntity.ok(bggService.searchBoardGames(query));
    }

    @GetMapping("get-bgg-thing/{bggId}")
    public ResponseEntity<BggThingDto> getBggThing(@PathVariable Long bggId) {
        return ResponseEntity.ok(bggService.getThingById(bggId));
    }
}
