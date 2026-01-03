package dev.apibaras.boardgamerental.service;



import dev.apibaras.boardgamerental.model.BoardGame;
import dev.apibaras.boardgamerental.model.Event;
import dev.apibaras.boardgamerental.repository.BoardGameRepository;
import dev.apibaras.boardgamerental.repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BoardGameService {

    private final BoardGameRepository boardGameRepository;

    private final EventRepository eventRepository;

    @Autowired
    public BoardGameService(BoardGameRepository boardGameRepository, EventRepository eventRepository) {
        this.boardGameRepository = boardGameRepository;
        this.eventRepository = eventRepository;
    }

    public List<BoardGame> getAll() {
        return boardGameRepository.findAll();
    }

    public BoardGame getById(Long id) {
        return boardGameRepository.getValidBoardGameById(id);
    }



    public void delete(Long id) {
        if(!boardGameRepository.existsById(id)){
            throw new EntityNotFoundException("BoardGame on id " + id + " not found");
        }
        boardGameRepository.deleteById(id);
    }


    public BoardGame save(Long EventId, BoardGame boardGame) {

        Event event = eventRepository.getValidEventById(EventId);
        boardGame.setEvent(event);

        return boardGameRepository.save(boardGame);
    }


}

