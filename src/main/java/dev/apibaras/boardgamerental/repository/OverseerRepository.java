package dev.apibaras.boardgamerental.repository;


import dev.apibaras.boardgamerental.model.Overseer;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OverseerRepository extends JpaRepository<Overseer, Long> {

    Optional<Overseer> findByUsername(String username);

    default Overseer getValidOverseerById(Long id) {
        return findById(id).orElseThrow(
                () -> new EntityNotFoundException("Overseer on id " + id + " not found"));
    }

    default Overseer getValidOverseerByUsername(String username) {
        return findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException("Overseer with username " + username + " not found"));
    }

}
