package uz.pdp.backend.repository.impl;

import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.NonNull;
import uz.pdp.backend.model.Order;
import uz.pdp.backend.model.Product;
import uz.pdp.backend.model.User;
import uz.pdp.backend.repository.BaseRepository;
import uz.pdp.backend.service.FileHelper;
import uz.pdp.backend.utils.FIleURLS;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderRepositoryImpl implements BaseRepository<Order, UUID> {
    @Getter
    private static final OrderRepositoryImpl instance = new OrderRepositoryImpl();

    private OrderRepositoryImpl() {}

    @Override
    public Boolean save(Order order) {
        List<Order> allOrders = getAllOrders();
        allOrders.add(order);
        setAllOrdersToFile(allOrders);
        return true;
    }

    @Override
    public Boolean update(Order order) {
        List<Order> collect = getAllOrders().stream().map(temp -> {
            if (temp.getId().equals(order.getId()))
                return order;
            return temp;
        }).collect(Collectors.toList());
        setAllOrdersToFile(collect);
        return true;
    }

    @Override
    public List<Order> findAll() {
        return getAllOrders();
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return getAllOrders().stream().filter(temp -> temp.getId().equals(id)).findFirst();
    }

    @NonNull
    private List<Order> getAllOrders() {
        List<Order> load = FileHelper.load(FIleURLS.ORDERS, new TypeToken<List<Order>>() {}.getType());
        return load == null ? new ArrayList<>() : load;
    }

    private void setAllOrdersToFile(List<Order> data) {
        FileHelper.write(FIleURLS.ORDERS, data);
    }
}
