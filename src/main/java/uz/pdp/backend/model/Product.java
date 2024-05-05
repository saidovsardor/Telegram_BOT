package uz.pdp.backend.model;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class Product implements Serializable {
    private final UUID id = UUID.randomUUID();
    private String productName;
    private Long price;
    private String description;
    private String photoUrl;
    private int count;
    private UUID categoryID;

    public Product(String productName, Long price, String description, String photoUrl, int count, UUID categoryID) {
        this.productName = productName;
        this.price = price;
        this.description = description;
        this.photoUrl = photoUrl;
        this.count = count;
        this.categoryID = categoryID;
    }
}
