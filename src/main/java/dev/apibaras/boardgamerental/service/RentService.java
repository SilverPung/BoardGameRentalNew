package dev.apibaras.boardgamerental.service;



import dev.apibaras.boardgamerental.model.boardgame.BoardGame;
import dev.apibaras.boardgamerental.model.boardgame.Rating;
import dev.apibaras.boardgamerental.model.event.Event;
import dev.apibaras.boardgamerental.model.rent.*;
import dev.apibaras.boardgamerental.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class RentService {

    private final RentRepository rentRepository;
    private final BoardGameRepository boardGameRepository;
    private final RenterRepository renterRepository;
    private final RatingRepository ratingRepository;
    private final RenterService renterService;


    @Autowired
    public RentService(RentRepository rentRepository, BoardGameRepository boardGameRepository, RenterRepository renterRepository, RenterService renterService, RatingRepository ratingRepository) {
        this.rentRepository = rentRepository;
        this.boardGameRepository = boardGameRepository;
        this.renterRepository = renterRepository;
        this.renterService = renterService;
        this.ratingRepository = ratingRepository;
    }

   public BoardGameRentedResponse rentBoardGame(Long eventId, @NotNull RentRequest rentRequest){

        log.info("Processing new rent for eventId: {} , renterBarcode: {} , boardGameId: {}", eventId, rentRequest.getRenterBarcode(), rentRequest.getBoardGameId());
        return handleNewRent(eventId, rentRequest);
   }

   public BoardGameRentedResponse returnBoardGame(Long eventId, @NotNull ReturnRequest returnRequest) {
       log.info("Processing return for eventId: {} , renterBarcode: {} , boardGameId: {}", eventId, returnRequest.getRenterBarcode(), returnRequest.getBoardGameId());
       return handleRentReturn(eventId, returnRequest);
   }


    public Page<RentResponse> getRentsForEvent(Long eventId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Rent> rentPage = rentRepository.findByEventIdAndReturnedFalse(eventId, pageRequest);
        return rentPage.map(RentResponse::new);

    }

    public RentResponse getRentDetails(Long eventId, Long rentId) {
        Optional<Rent> rentOpt = rentRepository.findByIdAndEventId(rentId, eventId);
        if(rentOpt.isEmpty()){
            log.error("Rent not found with id: {} for eventId: {}", rentId, eventId);
            throw new EntityNotFoundException("Rent not found with id: " + rentId + " for eventId: " + eventId);
        }
        return new RentResponse(rentOpt.get());
    }



    // ---------------------- Private Helper Methods --------------------- //
    private BoardGameRentedResponse handleRentReturn(Long eventId, ReturnRequest returnRequest){
        Optional<Renter> renterOpt = renterRepository.findRenterByBarcode(returnRequest.getRenterBarcode());
        if(renterOpt.isEmpty()){
            log.error("Renter not found with barcode: {}", returnRequest.getRenterBarcode());
            throw new EntityNotFoundException("Renter not found with barcode: " + returnRequest.getRenterBarcode());
        }
        Renter renter = renterOpt.get();

        Optional<Rent> rent = rentRepository.findByRenterIdAndBoardGameIdAndEventId(renter.getId(), returnRequest.getBoardGameId(), eventId);
        if(rent.isEmpty()){
            log.error("Rent not found for renterId: {} and boardGameId: {}", renter.getId(), returnRequest.getBoardGameId());
            throw new EntityNotFoundException("Rent not found for renterId: " + renter.getId() + " and boardGameId: " + returnRequest.getBoardGameId());
        }
        Rent existingRent = rent.get();
        if (existingRent.isReturned()){
            log.error("Board game already returned for renterId: {} and boardGameId: {}", renter.getId(), returnRequest.getBoardGameId());
            throw new IllegalStateException("Board game already returned for renterId: " + renter.getId() + " and boardGameId: " + returnRequest.getBoardGameId());
        }
        existingRent.returnGame();
        Rent updatedRent = rentRepository.save(existingRent);
        // Increase the available quantity
        BoardGame boardGame = updatedRent.getBoardGame();
        boardGame.setQuantityAvailable(boardGame.getQuantityAvailable() + 1);
        boardGameRepository.save(boardGame);
        log.info("Increased available quantity for boardGameId: {}. New quantityAvailable: {}", boardGame.getId(), boardGame.getQuantityAvailable());

        if(returnRequest.getRating() != null && returnRequest.getRating() >=1 && returnRequest.getRating() <= 10){
            ratingRepository.save(new Rating(
                    boardGame,
                    returnRequest.getRating()
            ));
        }


        log.info("Board game returned successfully for renterId: {} and boardGameId: {}", renter.getId(), returnRequest.getBoardGameId());
        return new BoardGameRentedResponse(updatedRent);

    }

    private BoardGameRentedResponse handleNewRent(Long eventId, RentRequest rentRequest){
        Optional<Renter> renterOpt = renterRepository.findRenterByBarcode(rentRequest.getRenterBarcode());
        Renter renter;
        if(renterOpt.isEmpty()){
            log.info("Renter not found with id: {}, creating new renter.", rentRequest.getRenterBarcode());
            renter = renterService.createRenterForEvent(eventId, rentRequest.getRenterBarcode());
        }
        else{
            renter = renterOpt.get();
        }
        Rent rent = new Rent();
        rent.setRenter(renter);
        Optional<BoardGame> boardGameOpt = boardGameRepository.findByIdAndEventId(rentRequest.getBoardGameId(), eventId);
        if(boardGameOpt.isEmpty()){
            log.error("Board game not found with id: {} for eventId: {}", rentRequest.getBoardGameId(), eventId);
            throw new EntityNotFoundException("Board game not found with id: " + rentRequest.getBoardGameId() + " for eventId: " + eventId);
        }
        BoardGame boardGame = boardGameOpt.get();
        if (boardGame.getQuantityAvailable() <= 0){
            log.error("Board game with id: {} is not available for rent.", rentRequest.getBoardGameId());
            throw new IllegalStateException("Board game with id: " + rentRequest.getBoardGameId() + " is not available for rent.");
        }
        // Decrease the available quantity
        boardGame.setQuantityAvailable(boardGame.getQuantityAvailable() - 1);
        boardGameRepository.save(boardGame);
        log.info("Decreased available quantity for boardGameId: {}. New quantityAvailable: {}", boardGame.getId(), boardGame.getQuantityAvailable());
        rent.setBoardGame(boardGame);
        rent.setEvent(boardGame.getEvent());
        rent.rentGame();
        Rent savedRent = rentRepository.save(rent);
        log.info("Board game rented successfully for renterId: {} and boardGameId: {}", renter.getId(), rentRequest.getBoardGameId());
        return new BoardGameRentedResponse(savedRent);

    }
}
