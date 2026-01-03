package dev.apibaras.boardgamerental.model.rent;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RentRequest {


    @NotNull
    boolean returned;

    @NotNull
    String renterBarcode;

    @NotNull
    long boardGameId;
}
