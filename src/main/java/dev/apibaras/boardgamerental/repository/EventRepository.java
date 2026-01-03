package dev.apibaras.boardgamerental.repository;


import dev.apibaras.boardgamerental.model.event.Event;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByOverseersId(long overseersId);

    default Event getValidEventById(Long id) {
        return findById(id).orElseThrow(
                () -> new EntityNotFoundException("Event on id " + id + " not found"));
    }

}
