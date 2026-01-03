package dev.apibaras.boardgamerental.controller;

import dev.apibaras.boardgamerental.model.rent.RentRequest;
import dev.apibaras.boardgamerental.model.rent.BoardGameRentedResponse;
import dev.apibaras.boardgamerental.model.rent.RentResponse;
import dev.apibaras.boardgamerental.service.RentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;


@Slf4j
@RequestMapping("/api/v1/events")
@RestController
public class RentController {


    private final RentService rentService;

    @Autowired
    public RentController(RentService rentService) {
        this.rentService = rentService;
    }

    @PreAuthorize("@AuthenticationService.hasAccessToEvent(#eventId) or hasRole('ADMIN')")
    @PutMapping("/{eventId}/rents")
    public ResponseEntity<BoardGameRentedResponse> rentBoardGame(@PathVariable Long eventId, @RequestBody @Valid RentRequest rentRequest) {
        return ResponseEntity.ok(rentService.rentBoardGame(eventId, rentRequest));

    }

    @PreAuthorize("@AuthenticationService.hasAccessToEvent(#eventId) or hasRole('ADMIN')")
    @GetMapping("/{eventId}/rents")
    public Page<RentResponse> listRents(
            @PathVariable Long eventId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return rentService.getRentsForEvent(eventId, page, size);
    }

    @PreAuthorize("@AuthenticationService.hasAccessToEvent(#eventId) or hasRole('ADMIN')")
    @GetMapping("{eventId}/rents/{rentId} ")
    public ResponseEntity<RentResponse> getRentDetails(@PathVariable Long eventId, @PathVariable Long rentId) {
        return ResponseEntity.ok(rentService.getRentDetails(eventId, rentId));
    }






}
