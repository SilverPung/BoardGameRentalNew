package dev.apibaras.boardgamerental.service;



import dev.apibaras.boardgamerental.model.Renter;
import dev.apibaras.boardgamerental.model.request.RenterRequest;
import dev.apibaras.boardgamerental.repository.EventRepository;
import dev.apibaras.boardgamerental.repository.RenterRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RenterService {

    private final RenterRepository renterRepository;

    private final EventRepository eventRepository;

    @Autowired
    public RenterService(RenterRepository renterRepository, EventRepository eventRepository) {
        this.renterRepository = renterRepository;
        this.eventRepository = eventRepository;
    }


    public List<Renter> getAll() {
        return renterRepository.findAll();
    }

    public Renter getById(Long id) {
        return renterRepository.getValidRenterById(id);
    }

    public Renter save(RenterRequest renterRequest) {
        Renter renter = new Renter();
        renter.setEvent(eventRepository.getValidEventById(renterRequest.getEventId()));
        renter.setData(renterRequest);
        return renterRepository.save(renter);
    }

    public void delete(Long id) {
        if(!renterRepository.existsById(id)){
            throw new EntityNotFoundException("Renter with id " + id + " not found");
        }
        renterRepository.deleteById(id);
    }

    public Renter update(Long id, RenterRequest renterRequest) {
        Renter renter = renterRepository.getValidRenterById(id);
        renter.setEvent(eventRepository.getValidEventById(renterRequest.getEventId()));
        renter.setData(renterRequest);
        return renterRepository.save(renter);
    }
}
