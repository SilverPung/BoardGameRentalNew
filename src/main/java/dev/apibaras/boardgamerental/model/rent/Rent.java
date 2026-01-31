package dev.apibaras.boardgamerental.model.rent;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import dev.apibaras.boardgamerental.model.boardgame.BoardGame;
import dev.apibaras.boardgamerental.model.event.Event;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Rent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private boolean returned;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date rentDate;

    @JsonIgnoreProperties("rents")
    @ManyToOne
    @JoinColumn(name = "renterId", nullable = false)
    private Renter renter;

    @JsonIgnoreProperties("rents")
    @ManyToOne
    @JoinColumn(name = "boardGameId", nullable = false)
    private BoardGame boardGame;

    @JsonIgnoreProperties("rents")
    @ManyToOne
    @JoinColumn(name = "eventId", nullable = false)
    private Event event;

    public void returnGame(){
        this.returned = true;
    }

    public void rentGame(){
        this.returned = false;
    }


    @PrePersist
    private void onCreate() {
        if (this.rentDate == null) {
            this.rentDate = new Date();
        }
    }
}
