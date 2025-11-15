package dev.apibaras.boardgamerental.repository;


import dev.apibaras.boardgamerental.model.Overseer;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OverseerRepository extends JpaRepository<Overseer, Long> {

    default Overseer getValidOverseerById(Long id) {
        return findById(id).orElseThrow(
                () -> new EntityNotFoundException("Overseer on id " + id + " not found"));
    }
}
