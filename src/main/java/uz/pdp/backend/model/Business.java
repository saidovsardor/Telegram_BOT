package uz.pdp.backend.model;

import lombok.Data;
import uz.pdp.backend.enums.BusinessType;

import java.io.Serializable;
import java.util.UUID;

@Data
public class Business implements Serializable {
    private final UUID id = UUID.randomUUID();
    private Long ownerId;
    private String businessName;
    private BusinessType businessType;
    private String balanceCardNumber;
    private boolean isDelete = false;

    public Business(Long ownerId, String businessName, BusinessType businessType) {
        this.ownerId = ownerId;
        this.businessName = businessName;
        this.businessType = businessType;
    }
}
