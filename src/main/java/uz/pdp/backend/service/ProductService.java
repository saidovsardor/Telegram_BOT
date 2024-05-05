package uz.pdp.backend.service;

import lombok.Getter;
import uz.pdp.backend.model.Category;
import uz.pdp.backend.model.Product;
import uz.pdp.backend.payload.CreateProductDTO;
import uz.pdp.backend.repository.impl.ProductsRepositoryImpl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProductService {
    private static final ProductsRepositoryImpl productRepository = ProductsRepositoryImpl.getInstance();
    private static final CategoryService categoryService = CategoryService.getInstance();
    @Getter
    private static final ProductService instance = new ProductService();

    private ProductService() {
    }

    public Product createProduct(CreateProductDTO createProductDTO) {
        Category category = categoryService.createCategory(createProductDTO.businessId(), createProductDTO.categoryName());
        Product product = new Product(createProductDTO.productName(), createProductDTO.price(), createProductDTO.description(), createProductDTO.photoUrl(), createProductDTO.amount(), category.getId());
        productRepository.save(product);
        return product;
    }

    public List<Product> getCategoriesAllProducts(UUID categoryId) {
        List<Product> products = productRepository.findAll();
        return products.stream().filter(temp -> temp.getCategoryID().equals(categoryId)).collect(Collectors.toList());
    }

    public Integer countAllProductsByBusiness(UUID businessId){
        List<Category> categories = categoryService.getAllCategoriesOfBusiness(businessId);
        int productCount = 0;
        for (Category category : categories) {
            productCount += getCategoriesAllProducts(category.getId()).size();
        }
        return productCount;
    }

}
