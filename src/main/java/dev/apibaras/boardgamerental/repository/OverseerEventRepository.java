package dev.apibaras.boardgamerental.repository;


import dev.apibaras.boardgamerental.model.OverseerEvent;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OverseerEventRepository extends JpaRepository<OverseerEvent, Long> {

    default OverseerEvent getValidOverseerEventById(Long id) {
        return findById(id).orElseThrow(
                () -> new EntityNotFoundException("OverseerEvent on id " + id + " not found"));
    }
}
