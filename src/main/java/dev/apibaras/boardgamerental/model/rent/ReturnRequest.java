package dev.apibaras.boardgamerental.model.rent;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReturnRequest {

    @NotNull
    String renterBarcode;

    @NotNull
    long boardGameId;

    Integer rating;
}
