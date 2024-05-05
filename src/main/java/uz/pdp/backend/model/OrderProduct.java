package uz.pdp.backend.model;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class OrderProduct implements Serializable {
    private final UUID id = UUID.randomUUID();
    private UUID productId;
    private int amount;
    private UUID orderId;

    public OrderProduct(UUID productId, int amount, UUID orderId) {
        this.productId = productId;
        this.amount = amount;   
        this.orderId = orderId;
    }
}
