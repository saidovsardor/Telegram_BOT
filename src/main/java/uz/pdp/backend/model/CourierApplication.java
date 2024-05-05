package uz.pdp.backend.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CourierApplication implements Serializable {
    private UUID id = UUID.randomUUID();
    private final long ownerId;
    private LocalDateTime createdTme;
    private boolean isActive;
    public CourierApplication(long ownerId, LocalDateTime createdTme, boolean isActive) {
        this.ownerId = ownerId;
        this.createdTme = createdTme;
        this.isActive = isActive;
    }
}
