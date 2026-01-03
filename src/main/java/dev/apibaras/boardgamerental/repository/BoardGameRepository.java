package dev.apibaras.boardgamerental.repository;



import dev.apibaras.boardgamerental.model.boardgame.BoardGame;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardGameRepository extends JpaRepository<BoardGame, Long> {


    Optional<BoardGame> findByIdAndEventId(Long id, Long eventId);

    Page<BoardGame> findByEventId(Long eventId, Pageable pageable);

    Long eventId(long eventId);
}
