package dev.apibaras.boardgamerental.controller;



import dev.apibaras.boardgamerental.model.boardgame.BoardGame;
import dev.apibaras.boardgamerental.model.boardgame.BoardGameRequest;
import dev.apibaras.boardgamerental.model.boardgame.BoardGameSearchResponse;
import dev.apibaras.boardgamerental.service.BoardGameService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/event/{eventId}/boardgames")
public class BoardGameController {


    private final BoardGameService boardGameService;

    @Autowired
    public BoardGameController(BoardGameService boardGameService) {
        this.boardGameService = boardGameService;
    }


    @PreAuthorize("@AuthenticationService.hasAccessToEvent(#eventId) or hasRole('ADMIN')")
    @GetMapping("")
    public ResponseEntity<Page<BoardGameSearchResponse>> getAllBoardGames(@PathVariable Long eventId,
                                                                          @RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(boardGameService.getAll(eventId, page, size));
    }

    @PreAuthorize("@AuthenticationService.hasAccessToEvent(#eventId) or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<BoardGameSearchResponse> getBoardGameById(@PathVariable Long id, @PathVariable Long eventId) {
        return ResponseEntity.ok(boardGameService.getById(id, eventId));
    }

    @PreAuthorize("@AuthenticationService.hasAccessToEvent(#eventId) or hasRole('ADMIN')")
    @PostMapping("")
    public ResponseEntity<BoardGameSearchResponse> saveBoardGame(@RequestBody BoardGameRequest boardGameRequest,@PathVariable Long eventId) {
        BoardGame boardGame = new BoardGame(boardGameRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(boardGameService.save(eventId, boardGame));
    }

    @PreAuthorize("@AuthenticationService.hasAccessToEvent(#eventId) or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoardGame(@PathVariable Long id,@PathVariable Long eventId) {
        boardGameService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("@AuthenticationService.hasAccessToEvent(#eventId) or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<BoardGameSearchResponse> updateBoardGame(@PathVariable Long id, @RequestBody BoardGameRequest boardGameRequest,@PathVariable Long eventId) {
        BoardGame boardGame = new BoardGame(boardGameRequest);
        boardGame.setId(id);
        return ResponseEntity.ok(boardGameService.save(eventId,boardGame));
    }


}
