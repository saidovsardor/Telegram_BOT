package uz.pdp.backend.model;

import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.Location;

import java.io.Serializable;
import java.util.UUID;

@Data
public class BusinessLocation implements Serializable {
    private final UUID id = UUID.randomUUID();
    private Location location; // org.telegram.telegrambots.meta.api.objects.Location;
    private UUID businessId;
    private boolean isDelete = false;

    public BusinessLocation(Location location, UUID businessId) {
        this.location = location;
        this.businessId = businessId;
    }
}
