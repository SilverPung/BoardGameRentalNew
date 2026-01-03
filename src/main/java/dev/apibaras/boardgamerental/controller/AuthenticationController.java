package dev.apibaras.boardgamerental.controller;

import dev.apibaras.boardgamerental.model.logon.Overseer;
import dev.apibaras.boardgamerental.model.logon.LoginOverseerDto;
import dev.apibaras.boardgamerental.model.logon.RegisterOverseerDto;
import dev.apibaras.boardgamerental.service.AuthenticationService;
import dev.apibaras.boardgamerental.security.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/auth")
@RestController
@Slf4j
public class AuthenticationController {

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    @Value("${security.jwt.refresh-expiration-time}")
    private long refreshExpiration;

    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    private final UserDetailsService userDetailsService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> register(@RequestBody RegisterOverseerDto registerUserDto) {
        authenticationService.signup(registerUserDto);

        return ResponseEntity.ok("User registered successfully");
    }



    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@RequestBody LoginOverseerDto loginUserDto, HttpServletResponse response) {
        Overseer authenticatedUser = authenticationService.authenticate(loginUserDto);

        addJwtCookie(response, jwtService.generateToken(authenticatedUser));
        addRefreshTokenCookie(response, jwtService.generateRefreshToken(authenticatedUser));

        return ResponseEntity.ok("Logged in");
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractTokenFromRequest(request, "REFRESH_TOKEN");
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No refresh token");
        }

        try {
            String username = jwtService.extractUsername(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (!jwtService.isRefreshTokenValid(refreshToken, userDetails)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
            }

            String newAccessToken = jwtService.generateToken(userDetails);
            String newRefreshToken = jwtService.generateRefreshToken(userDetails); // rotacja

            addJwtCookie(response, newAccessToken);
            addRefreshTokenCookie(response, newRefreshToken);

            return ResponseEntity.ok("Tokens refreshed");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
    }

    private String extractTokenFromRequest(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (cookieName.equals(c.getName())) {
                    String v = c.getValue();
                    if (v != null && !v.isBlank()) return v;
                }
            }
        }


        return null;
    }




    private void addJwtCookie(HttpServletResponse response, String jwt) {
        Cookie cookie = new Cookie("AUTH_TOKEN", jwt);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
    }

    private void addRefreshTokenCookie(HttpServletResponse response, String jwt) {
        Cookie cookie = new Cookie("REFRESH_TOKEN", jwt);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
    }






}