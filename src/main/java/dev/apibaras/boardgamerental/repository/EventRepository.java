package dev.apibaras.boardgamerental.repository;


import dev.apibaras.boardgamerental.model.event.Event;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Page<Event> findByOverseersId(Long overseerId, Pageable pageable);

    default Event getValidEventById(Long id) {
        return findById(id).orElseThrow(
                () -> new EntityNotFoundException("Event on id " + id + " not found"));
    }

}
