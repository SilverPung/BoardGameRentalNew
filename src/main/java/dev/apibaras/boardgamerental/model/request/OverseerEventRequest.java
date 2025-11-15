package dev.apibaras.boardgamerental.model.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OverseerEventRequest {

    private long overseerId;
    private long eventId;
    private String permissionsType;


}
