package dev.apibaras.boardgamerental.service;


import dev.apibaras.boardgamerental.model.logon.Overseer;
import dev.apibaras.boardgamerental.repository.OverseerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class OverseerService {

    private final OverseerRepository overseerRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public OverseerService(OverseerRepository overseerRepository, PasswordEncoder passwordEncoder) {
        this.overseerRepository = overseerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Overseer> getAll() {
        return overseerRepository.findAll();

    }

    public Overseer getById(Long id) {
        return overseerRepository.getValidOverseerById(id);
    }

    public Overseer save(Overseer overseer) {
        return saveCode(overseer);
    }

    public void delete(Long id) {
        if(!overseerRepository.existsById(id)){
            throw new EntityNotFoundException("Overseer on id " + id + " not found");
        }
        overseerRepository.deleteById(id);
    }

    public Overseer update(long id,Overseer overseer) {
        Overseer overseerToUpdate = overseerRepository.getValidOverseerById(id);
        overseer.setId(id);
        if(overseer.getPassword() == null){
            overseer.setPassword(overseerToUpdate.getPassword());
            return overseerRepository.save(overseer);
        }
        return saveCode(overseer);
    }

    private Overseer saveCode(Overseer overseer) {
        if (overseer.getPassword() != null) {
            overseer.setPassword(passwordEncoder.encode(overseer.getPassword()));
        }
        return overseerRepository.save(overseer);
    }


}
