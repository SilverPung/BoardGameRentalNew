package dev.apibaras.boardgamerental.service;


import dev.apibaras.boardgamerental.model.boardgame.BoardGameSearchResponse;
import org.audux.bgg.BggClient;
import org.audux.bgg.common.ThingType;
import org.audux.bgg.response.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import org.audux.bgg.response.SearchResults;


import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class BggService {


    @Value("${bgg.api.token}")
    private String bggApiToken;

    public static CompletableFuture<List<Thing>> fetch10Boardgames(String query, String bearerToken) {
        // Set the required BGG auth token
        BggClient.authToken(bearerToken);

        // 1) Async search request (boardgame type)
        CompletableFuture<Response<SearchResults>> searchFuture =
                BggClient.search(query, new ThingType[]{ThingType.BOARD_GAME}, false)
                        .callAsync();

        // 2) When search completes, extract up to 10 boardgame IDs and fetch full thing objects
        return searchFuture.thenCompose(searchResults -> {
            if (searchResults == null || searchResults.getData() == null) {
                return CompletableFuture.completedFuture(List.of());
            }

            var ids = searchResults.getData().getResults().stream()
                    .filter(item -> item.getType() == ThingType.BOARD_GAME)
                    .limit(10)
                    .map(SearchResult::getId)
                    .toArray(Integer[]::new);

            // If no IDs, return empty list
            if (ids.length == 0) {
                return CompletableFuture.completedFuture(List.of());
            }

            // 3) Async “things” request: pass the IDs to fetch full object data
            CompletableFuture<Response<Things>> thingsFuture =
                    BggClient.things(
                            /* ids= */ ids,
                            /* types= */ new ThingType[]{ThingType.BOARD_GAME},
                            /* stats= */ true,    // include rating/stats if you want
                            /* versions= */ true, // include version info
                            /* videos= */ false,
                            /* marketplace= */ false,
                            /* comments= */ false,
                            /* ratingComments= */ false
                    ).callAsync();

            // 4) Extract the actual list of Thing objects
            return thingsFuture.thenApply(thingsResponse -> {
                if (thingsResponse == null || thingsResponse.getData() == null) {
                    return List.of();
                }
                return List.copyOf(thingsResponse.getData().getThings());
            });
        });
    }

    public Set<BoardGameSearchResponse> searchBoardGames(String query) {
        CompletableFuture<List<Thing>> thingsFuture = fetch10Boardgames(query, bggApiToken);
        List<Thing> things; // Wait for completion

        try {
            things = thingsFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }


        // Map Thing objects to BoardGame model
        return things.stream().map(thing -> {
            BoardGameSearchResponse bg = new BoardGameSearchResponse();
            bg.setName(thing.getName());
            bg.setImage(thing.getImage());
            bg.setThumbnail(thing.getThumbnail());
            bg.setQuantity(1);
            bg.setQuantityAvailable(1);
            return bg;
        }).collect(Collectors.toSet());
    }
}

