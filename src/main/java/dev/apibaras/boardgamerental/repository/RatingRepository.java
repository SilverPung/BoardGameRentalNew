package dev.apibaras.boardgamerental.repository;


import dev.apibaras.boardgamerental.model.boardgame.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
}
