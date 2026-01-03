package dev.apibaras.boardgamerental.repository;


import dev.apibaras.boardgamerental.model.rent.Renter;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RenterRepository extends JpaRepository<Renter, Long> {

    default Renter getValidRenterById(Long id) {
        return findById(id).orElseThrow(
                () -> new EntityNotFoundException("Renter on id " + id + " not found"));
    }

    Optional<Renter> findRenterByBarcode(String barcode);
}
