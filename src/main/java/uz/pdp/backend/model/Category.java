package uz.pdp.backend.model;

import lombok.Data;
import uz.pdp.backend.enums.BusinessType;

import java.io.Serializable;
import java.util.UUID;

@Data
public class Category implements Serializable {
    private final UUID id = UUID.randomUUID();
    private String categoryName;
    private UUID businessId;

    public Category(String categoryName, UUID businessId) {
        this.categoryName = categoryName;
        this.businessId = businessId;
    }
}
