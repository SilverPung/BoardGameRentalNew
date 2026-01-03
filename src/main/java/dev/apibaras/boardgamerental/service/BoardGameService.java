package dev.apibaras.boardgamerental.service;



import dev.apibaras.boardgamerental.model.boardgame.BoardGame;
import dev.apibaras.boardgamerental.model.boardgame.BoardGameSearchResponse;
import dev.apibaras.boardgamerental.model.event.Event;
import dev.apibaras.boardgamerental.repository.BoardGameRepository;
import dev.apibaras.boardgamerental.repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class BoardGameService {

    private final BoardGameRepository boardGameRepository;

    private final EventRepository eventRepository;

    @Autowired
    public BoardGameService(BoardGameRepository boardGameRepository, EventRepository eventRepository) {
        this.boardGameRepository = boardGameRepository;
        this.eventRepository = eventRepository;
    }

    public Page<BoardGameSearchResponse> getAll(Long eventId, int page, int size) {
        return boardGameRepository.findByEventId(eventId, PageRequest.of(page, size))
                .map(BoardGameSearchResponse::new);
    }

    public BoardGameSearchResponse getById(Long id, Long eventId) {
        Optional<BoardGame> boardGameOptional = boardGameRepository.findByIdAndEventId(id,eventId);
        if (boardGameOptional.isPresent()) {
            BoardGame boardGame = boardGameOptional.get();
            return new BoardGameSearchResponse(boardGame);
        }
        throw new EntityNotFoundException("BoardGame on id " + id + " not found");
    }



    public void delete(Long id) {
        if(!boardGameRepository.existsById(id)){
            throw new EntityNotFoundException("BoardGame on id " + id + " not found");
        }
        boardGameRepository.deleteById(id);
    }


    public BoardGameSearchResponse save(Long EventId, BoardGame boardGame) {

        Event event = eventRepository.getValidEventById(EventId);
        boardGame.setEvent(event);

        return new BoardGameSearchResponse(boardGameRepository.save(boardGame));
    }


}

