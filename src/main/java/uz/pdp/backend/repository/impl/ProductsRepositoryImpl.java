package uz.pdp.backend.repository.impl;

import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.NonNull;
import uz.pdp.backend.model.Product;
import uz.pdp.backend.repository.BaseRepository;
import uz.pdp.backend.service.FileHelper;
import uz.pdp.backend.utils.FIleURLS;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProductsRepositoryImpl implements BaseRepository<Product, UUID> {
    @Getter
    private static final ProductsRepositoryImpl instance = new ProductsRepositoryImpl();
    private ProductsRepositoryImpl(){}
    @Override
    public Boolean save(Product product) {
        List<Product> products = getAllProducts();
        products.add(product);
        setAllProductsToFile(products);
        return true;
    }

    @Override
    public Boolean update(Product product) {
        List<Product> products = getAllProducts();
        products.stream().map(
                temp -> {
                    if(temp.getId().equals(product.getId()))
                        return product;
                    return temp;
                }
        ).collect(Collectors.toList());

        setAllProductsToFile(products);
        return true;
    }

    @Override
    public List<Product> findAll() {
        return getAllProducts();
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return getAllProducts().stream().filter(temp -> temp.getId().equals(id)).findFirst();
    }

    @NonNull
    private List<Product> getAllProducts() {
        List<Product> load = FileHelper.load(FIleURLS.PRODUCTS, new TypeToken<List<Product>>() {
        }.getType());
        return load == null ? new ArrayList<>() : load;
    }

    private void setAllProductsToFile(List<Product> data) {
        FileHelper.write(FIleURLS.PRODUCTS, data);
    }
}
