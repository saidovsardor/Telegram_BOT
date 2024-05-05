package uz.pdp.backend.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Balance implements Serializable {
    private final String cardNumber;
    private Long ownerId;
    private Long balance;
    private boolean isDelete = false;

    public Balance(String cardNumber, Long ownerId, Long balance) {
        this.cardNumber = cardNumber;
        this.ownerId = ownerId;
        this.balance = balance;
    }
}
