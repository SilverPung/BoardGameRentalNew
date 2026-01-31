package dev.apibaras.boardgamerental.model.boardgame;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardGameRequest {



    @NotNull
    String barcode;

    @NotNull
    String name;


    String description;
    String imageUrl;
    String thumbnailUrl;
    String publisher;
    boolean isTop30;


    String notes;

    @NotNull
    @PositiveOrZero
    int quantity;

    @NotNull
    @PositiveOrZero
    int quantityAvailable;
}
