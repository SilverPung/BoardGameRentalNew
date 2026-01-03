package dev.apibaras.boardgamerental.model.boardgame;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.apibaras.boardgamerental.model.event.Event;
import dev.apibaras.boardgamerental.model.rent.Rent;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;


import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BoardGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String barcode;

    @NotNull
    private String name;

    private String notes;

    private int quantity;
    private int quantityAvailable;

    // bgg data
    private String description;
    private String imageUrl;
    private String thumbnailUrl;
    private String publisher;




    @JsonIgnoreProperties("boardGames")
    @ManyToOne
    @JoinColumn(name = "eventId", nullable = false)
    private Event event;


    @JsonIgnoreProperties("boardGame")
    @OneToMany(mappedBy = "boardGame", cascade = CascadeType.REMOVE)
    private Set<Rent> rents;

    public BoardGame(Event event, String publisher, String thumbnailUrl, String imageUrl, String description, int quantityAvailable, int quantity, String notes, String name, String barcode) {
        this.event = event;
        this.publisher = publisher;
        this.thumbnailUrl = thumbnailUrl;
        this.imageUrl = imageUrl;
        this.description = description;
        this.quantityAvailable = quantityAvailable;
        this.quantity = quantity;
        this.notes = notes;
        this.name = name;
        this.barcode = barcode;
    }

    @Override
    public String toString() {
        return "BoardGame{" +
                "id=" + id +
                ", barcode='" + barcode + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", quantity=" + quantity +
                ", quantityAvailable=" + quantityAvailable +
                '}';
    }

    public BoardGame(BoardGameRequest boardGameRequest) {
        this.barcode = boardGameRequest.getBarcode();
        this.name = boardGameRequest.getName();
        this.description = boardGameRequest.getDescription();
        this.notes = boardGameRequest.getNotes();
        this.quantity = boardGameRequest.getQuantity();
        this.quantityAvailable = boardGameRequest.getQuantityAvailable();
        this.imageUrl = boardGameRequest.getImageUrl();
        this.thumbnailUrl = boardGameRequest.getThumbnailUrl();
        this.publisher = boardGameRequest.getPublisher();

    }





}
