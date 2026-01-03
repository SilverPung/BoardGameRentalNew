package dev.apibaras.boardgamerental.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegisterOverseerDto {

    private String username;
    private String password;
    private String email;
}
