package dev.apibaras.boardgamerental.model.dto;


import dev.apibaras.boardgamerental.model.BoardGame;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardGameSearchResponse {

    public String name;
    public String image;
    public String thumbnail;
    public int quantity;
    public int quantityAvailable;

    public BoardGameSearchResponse(BoardGame boardGame) {
        this.name = boardGame.getName();
        this.image = boardGame.getImageUrl();
        this.thumbnail = boardGame.getThumbnailUrl();
        this.quantity = boardGame.getQuantity();
        this.quantityAvailable = boardGame.getQuantityAvailable();
    }


}
