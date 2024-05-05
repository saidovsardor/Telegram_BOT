package uz.pdp.backend.service;

import lombok.Getter;
import uz.pdp.backend.model.Category;
import uz.pdp.backend.repository.impl.CategoryRepositoryImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class CategoryService {
    private static final CategoryRepositoryImpl categoryRepository = CategoryRepositoryImpl.getInstance();
    @Getter
    private static final CategoryService instance = new CategoryService();
    private CategoryService(){}

    public Category createCategory(UUID businessId, String categoryName){
        List<Category> categories = categoryRepository.findAll();
        Optional<Category> optionalCategory = categories.stream()
                .filter(temp -> temp.getBusinessId().equals(businessId))
                .filter(temp -> temp.getCategoryName().equals(categoryName))
                .findFirst();

        if(optionalCategory.isEmpty()){
            Category category = new Category(categoryName, businessId);
            categoryRepository.save(category);
            return category;
        }

        return optionalCategory.get();
    }

    public List<Category> getAllCategoriesOfBusiness(UUID businessId){
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().filter(temp -> temp.getBusinessId().equals(businessId)).collect(Collectors.toList());
    }
}
