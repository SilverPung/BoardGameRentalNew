package dev.apibaras.boardgamerental.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RentRequest {

    boolean returned;
    long renterId;
    long boardGameId;
}
