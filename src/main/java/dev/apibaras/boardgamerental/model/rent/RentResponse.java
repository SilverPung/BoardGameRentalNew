package dev.apibaras.boardgamerental.model.rent;


import dev.apibaras.boardgamerental.model.boardgame.BoardGameSearchResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RentResponse {



    BoardGameSearchResponse boardGameSearchResponse;

    String renterBarcode;

    String rentDate;

    public RentResponse(Rent rent) {
        this.boardGameSearchResponse = new BoardGameSearchResponse(rent.getBoardGame());
        this.renterBarcode = rent.getRenter().getBarcode();
        this.rentDate = rent.getRentDate().toString();
    }


}
