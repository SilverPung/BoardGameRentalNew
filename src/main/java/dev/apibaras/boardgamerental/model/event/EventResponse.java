package dev.apibaras.boardgamerental.model.event;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {

    @NotNull
    private String name;

    private String description;

    private long id;

    public EventResponse(Event savedEvent) {
        this.id = savedEvent.getId();
        this.name = savedEvent.getName();
        this.description = savedEvent.getDescription();
    }
}
