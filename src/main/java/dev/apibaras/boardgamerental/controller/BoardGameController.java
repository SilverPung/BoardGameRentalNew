package dev.apibaras.boardgamerental.controller;



import dev.apibaras.boardgamerental.model.BoardGame;
import dev.apibaras.boardgamerental.model.request.BoardGameRequest;
import dev.apibaras.boardgamerental.service.BoardGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/boardgame")
public class BoardGameController {


    private final BoardGameService boardGameService;

    @Autowired
    public BoardGameController(BoardGameService boardGameService) {
        this.boardGameService = boardGameService;
    }

    @GetMapping("")
    public ResponseEntity<List<BoardGame>> getAllBoardGames() {
        return ResponseEntity.ok(boardGameService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardGame> getBoardGameById(@PathVariable Long id) {
        return ResponseEntity.ok(boardGameService.getById(id));
    }

    @PostMapping("")
    public ResponseEntity<BoardGame> saveBoardGame(@RequestBody BoardGameRequest boardGameRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(boardGameService.save(boardGameRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoardGame(@PathVariable Long id) {
        boardGameService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardGame> updateBoardGame(@PathVariable Long id, @RequestBody BoardGameRequest boardGameRequest) {
        return ResponseEntity.ok(boardGameService.update(id, boardGameRequest));
    }
}
