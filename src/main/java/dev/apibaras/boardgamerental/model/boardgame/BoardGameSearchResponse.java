package dev.apibaras.boardgamerental.model.boardgame;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardGameSearchResponse {

    public long boardGameId;
    public String name;
    public String image;
    public String thumbnail;
    public int quantity;
    public int quantityAvailable;
    public boolean isTop30;

    public BoardGameSearchResponse(BoardGame boardGame) {
        this.boardGameId = boardGame.getId();
        this.name = boardGame.getName();
        this.image = boardGame.getImageUrl();
        this.thumbnail = boardGame.getThumbnailUrl();
        this.quantity = boardGame.getQuantity();
        this.quantityAvailable = boardGame.getQuantityAvailable();
        this.isTop30 = boardGame.isTop30();
    }




}
