package dev.apibaras.boardgamerental.repository;


import dev.apibaras.boardgamerental.model.Overseer;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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


    @Query("select case when count(ev) > 0 then true else false end " +
            "from Event ev join ev.overseers o " +
            "where ev.id = :eventId and o.id = :overseerId")
    boolean existsByEventIdAndOverseerId(@Param("eventId") long eventId,
                                         @Param("overseerId") long overseerId);

}
