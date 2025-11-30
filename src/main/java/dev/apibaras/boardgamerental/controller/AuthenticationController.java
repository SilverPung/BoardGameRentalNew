package dev.apibaras.boardgamerental.controller;

import dev.apibaras.boardgamerental.model.Overseer;
import dev.apibaras.boardgamerental.model.request.LoginOverseerDto;
import dev.apibaras.boardgamerental.model.request.RegisterOverseerDto;
import dev.apibaras.boardgamerental.service.AuthenticationService;
import dev.apibaras.boardgamerental.security.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Overseer> register(@RequestBody RegisterOverseerDto registerUserDto) {
        Overseer registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }

    private void addJwtCookie(HttpServletResponse response, String jwt) {
        Cookie cookie = new Cookie("AUTH_TOKEN", jwt);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@RequestBody LoginOverseerDto loginUserDto, HttpServletResponse response) {
        Overseer authenticatedUser = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);

        addJwtCookie(response, jwtToken);

        return ResponseEntity.ok("Logged in");
    }
}