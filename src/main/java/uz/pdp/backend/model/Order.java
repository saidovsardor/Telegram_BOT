package uz.pdp.backend.model;

import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.Location;

import java.io.Serializable;
import java.util.UUID;

@Data
public class Order implements Serializable {
    private final UUID id = UUID.randomUUID();
    private final Long ownerId;
    private Long deliveryPrice;
    private Long actualPrice;
    private Long servicePrice;
    private Location locationFrom;
    private Location locationTo;
    private boolean isActive = false;
    private boolean isDelete = false;

    public Order(Long ownerId) {
        this.ownerId = ownerId;
    }
}
