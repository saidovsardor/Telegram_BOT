package uz.pdp.backend.service;

import lombok.Getter;
import uz.pdp.backend.model.Order;

public class OrderService {
    @Getter
    private static final OrderService instance = new OrderService();

    private OrderService() {}

    public Order orderVerify(Long userId){
        return null;
    }
}
