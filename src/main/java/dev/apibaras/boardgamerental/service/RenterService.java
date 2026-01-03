package dev.apibaras.boardgamerental.service;




import dev.apibaras.boardgamerental.exception.UniqueValueException;
import dev.apibaras.boardgamerental.model.event.Event;
import dev.apibaras.boardgamerental.model.rent.Renter;
import dev.apibaras.boardgamerental.repository.EventRepository;
import dev.apibaras.boardgamerental.repository.RenterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class RenterService {

    private final RenterRepository renterRepository;

    private final EventRepository eventRepository;

    @Autowired
    public RenterService(RenterRepository renterRepository, EventRepository eventRepository) {
        this.renterRepository = renterRepository;
        this.eventRepository = eventRepository;
    }


    public Renter createRenterForEvent(Long eventId, String renterBarcode){
        Renter renter = new Renter(renterBarcode, "user-" + renterBarcode);
        Event event = eventRepository.getValidEventById(eventId);
        renter.setEvent(event);
        try{
            renterRepository.save(renter);
        } catch (DataIntegrityViolationException e){
            throw new UniqueValueException("Renter already exists with id: " + renterBarcode);
        }
        return renter;

    }
}
