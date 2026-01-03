package dev.apibaras.boardgamerental.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import dev.apibaras.boardgamerental.model.dto.RentRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Rent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private boolean returned;

    @JsonIgnoreProperties("rents")
    @ManyToOne
    @JoinColumn(name = "renterId", nullable = false)
    private Renter renter;

    @JsonIgnoreProperties("rents")
    @ManyToOne
    @JoinColumn(name = "boardGameId", nullable = false)
    private BoardGame boardGame;

    public void setData(RentRequest rentRequest){
        this.returned = rentRequest.isReturned();
    }
}
