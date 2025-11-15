package dev.apibaras.boardgamerental.model.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardGameRequest {

    long eventId;
    String barcode;
    String name;
    String description;
    String notes;
    int quantity;
    int quantityAvailable;
}
