package uz.pdp.backend.model;

import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.Location;
import uz.pdp.backend.enums.BusinessType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BusinessApplication implements Serializable {
    private final UUID id = UUID.randomUUID();
    private Long ownerId;
    private String businessName;
    private BusinessType businessType;
    private Location location;
    private LocalDateTime createTime = LocalDateTime.now();
    private boolean isActive = true;

    public BusinessApplication(Long ownerId, String businessName, BusinessType businessType, Location location) {
        this.ownerId = ownerId;
        this.businessName = businessName;
        this.businessType = businessType;
        this.location = location;
    }
}
