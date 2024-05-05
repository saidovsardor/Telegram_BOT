package uz.pdp.backend.model;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class Comment implements Serializable {
    private final UUID id = UUID.randomUUID();
    private String comment;
    private UUID businessId;
    private boolean isActive = true;

    public Comment(String comment, UUID businessId) {
        this.comment = comment;
        this.businessId = businessId;
    }
}
