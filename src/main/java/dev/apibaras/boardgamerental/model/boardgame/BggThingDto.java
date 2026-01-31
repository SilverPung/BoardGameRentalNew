package dev.apibaras.boardgamerental.model.boardgame;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BggThingDto {
    private long id;
    private String name;
    private String image;
    private String thumbnail;
    private String description;
    private String publisher;
    private int yearPublished;
    private int minPlayers;
    private int maxPlayers;
    private int playingTime;
    private double averageRating;
}

