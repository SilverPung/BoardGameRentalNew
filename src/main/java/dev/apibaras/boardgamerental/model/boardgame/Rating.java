package dev.apibaras.boardgamerental.model.boardgame;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int value;

    @ManyToOne
    @JoinColumn(name = "boardGameId", nullable = false)
    private BoardGame boardGame;

    public Rating(BoardGame boardGame, int value) {
        this.boardGame = boardGame;
        this.value = value;
    }


}
