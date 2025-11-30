package dev.apibaras.boardgamerental.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.apibaras.boardgamerental.model.request.BoardGameRequest;
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

    private String description;

    private String notes;


    private int quantity;
    private int quantityAvailable;

    public BoardGame(String barcode, String name, String description, String notes, int quantity, int quantityAvailable) {
        this.barcode = barcode;
        this.name = name;
        this.description = description;
        this.notes = notes;
        this.quantity = quantity;
        this.quantityAvailable = quantityAvailable;
    }

    @JsonIgnoreProperties("boardGames")
    @ManyToOne
    @JoinColumn(name = "eventId", nullable = false)
    private Event event;


    @JsonIgnoreProperties("boardGame")
    @OneToMany(mappedBy = "boardGame", cascade = CascadeType.REMOVE)
    private Set<Rent> rents;

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
    }





}
