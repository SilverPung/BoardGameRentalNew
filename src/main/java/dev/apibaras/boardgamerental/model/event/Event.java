package dev.apibaras.boardgamerental.model.event;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.apibaras.boardgamerental.model.boardgame.BoardGame;
import dev.apibaras.boardgamerental.model.logon.Overseer;
import dev.apibaras.boardgamerental.model.rent.Rent;
import dev.apibaras.boardgamerental.model.rent.Renter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@Entity
@NoArgsConstructor
public class Event {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String name;
    private String description;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date date;

    @JsonIgnoreProperties("events")
    @ManyToMany
    @JoinTable(
            name = "event_overseer",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "overseer_id")
    )
    private Set<Overseer> overseers = new HashSet<>();

    @JsonIgnoreProperties("event")
    @OneToMany(mappedBy = "event", cascade = CascadeType.REMOVE)
    private Set<BoardGame> boardGames = new HashSet<>();

    @JsonIgnoreProperties("event")
    @OneToMany(mappedBy = "event", cascade = CascadeType.REMOVE)
    private Set<Renter> renters = new HashSet<>();

    @JsonIgnoreProperties("event")
    @OneToMany(mappedBy = "event", cascade = CascadeType.REMOVE)
    private Set<Rent> rents = new HashSet<>();


    public Event(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                '}';
    }

    public void addOverseer(Overseer overseer) {
        if (overseer != null && !this.overseers.contains(overseer)) {
            this.overseers.add(overseer);
        }
    }

    @PrePersist
    private void onCreate() {
        if (this.date == null) {
            this.date = new Date();
        }
    }
}
