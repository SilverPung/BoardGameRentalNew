package dev.apibaras.boardgamerental.service;


import dev.apibaras.boardgamerental.model.logon.Overseer;
import dev.apibaras.boardgamerental.model.logon.LoginOverseerDto;
import dev.apibaras.boardgamerental.model.logon.RegisterOverseerDto;
import dev.apibaras.boardgamerental.repository.OverseerRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("AuthenticationService")
public class AuthenticationService {

    private final OverseerRepository overseerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            OverseerRepository overseerRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.overseerRepository = overseerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Overseer signup(RegisterOverseerDto input) {
        Overseer overseer = new Overseer();
        overseer.setUsername(input.getUsername());
        overseer.setPassword(passwordEncoder.encode(input.getPassword()));
        overseer.setEmail(input.getEmail());

        return overseerRepository.save(overseer);
    }

    public Overseer authenticate(LoginOverseerDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getUsername(),
                        input.getPassword()
                )
        );
        return overseerRepository.getValidOverseerByUsername(input.getUsername());

    }

    public boolean hasAccessToEvent(Long eventId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return false;
        String username = auth.getName();
        Overseer overseer = overseerRepository.getValidOverseerByUsername(username);
        return overseerRepository.existsByEventIdAndOverseerId(overseer.getId(), eventId);
    }

}