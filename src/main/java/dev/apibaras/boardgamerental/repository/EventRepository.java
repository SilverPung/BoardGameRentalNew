package dev.apibaras.boardgamerental.repository;


import dev.apibaras.boardgamerental.model.Event;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    default Event getValidEventById(Long id) {
        return findById(id).orElseThrow(
                () -> new EntityNotFoundException("Event on id " + id + " not found"));
    }
}
