package dev.apibaras.boardgamerental.model.rent;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.apibaras.boardgamerental.model.event.Event;
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
public class Renter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(name = "barcode", nullable = false, unique = true)
    private String barcode;

    @NotNull
    private String userName;

    @JsonIgnoreProperties("renter")
    @ManyToOne
    @JoinColumn(name = "eventId", nullable = false)
    private Event event;


    @JsonIgnoreProperties("renter")
    @OneToMany(mappedBy = "renter", cascade = CascadeType.REMOVE)
    private Set<Rent> rentedGames;

    public Renter(String barcode, String userName) {
        this.barcode = barcode;
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "Renter{" +
                "id=" + id +
                ", barcode='" + barcode + '\'' +
                ", name='" + userName + '\'' +
                '}';
    }
}
