package dev.apibaras.boardgamerental.model.rent;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardGameRentedResponse {


    @NotNull
    long renterId;

    @NotNull
    long boardGameId;

    @NotNull
    String message;


    public BoardGameRentedResponse(Rent rent) {
        this.renterId = rent.getRenter().getId();
        this.boardGameId = rent.getBoardGame().getId();
        this.message = rent.isReturned() ? "Board game returned successfully." : "Board game rented successfully.";

    }
}
