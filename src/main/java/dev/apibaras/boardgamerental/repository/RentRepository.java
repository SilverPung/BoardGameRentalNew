package dev.apibaras.boardgamerental.repository;



import dev.apibaras.boardgamerental.model.rent.Rent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RentRepository extends JpaRepository<Rent, Long> {

    default Rent getValidRentById(Long id) {
        return findById(id).orElseThrow(
                () -> new jakarta.persistence.EntityNotFoundException("Rent on id " + id + " not found"));
    }

    // Returns an Optional containing the Rent if one exists matching both renterId and boardGameId
    Optional<Rent> findByRenterIdAndBoardGameIdAndEventId(long renter_id, long boardGame_id, long event_id);

    Page<Rent> findByEventId(Long eventId, Pageable pageable);

    Page<Rent> findByEventIdAndReturnedFalse(Long eventId, Pageable pageable);

    Optional<Rent> findByIdAndEventId(Long rentId, Long eventId);
}
