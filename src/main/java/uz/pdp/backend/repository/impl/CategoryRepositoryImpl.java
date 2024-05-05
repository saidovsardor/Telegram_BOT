package uz.pdp.backend.repository.impl;

import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.NonNull;
import uz.pdp.backend.model.Business;
import uz.pdp.backend.model.Category;
import uz.pdp.backend.repository.BaseRepository;
import uz.pdp.backend.service.FileHelper;
import uz.pdp.backend.utils.FIleURLS;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class CategoryRepositoryImpl implements BaseRepository<Category, UUID> {
    @Getter
    private static final CategoryRepositoryImpl instance = new CategoryRepositoryImpl();
    private CategoryRepositoryImpl(){}
    @Override
    public Boolean save(Category category) {
        List<Category> categories = getAllCategories();
        categories.add(category);
        setAllCategoriesToFile(categories);
        return true;
    }

    @Override
    public Boolean update(Category category) {
        List<Category> categories = getAllCategories();
        categories.stream().map(
                temp -> {
                    if(temp.getId().equals(category.getId()))
                        return category;
                    return temp;
                }
        ).collect(Collectors.toList());

        setAllCategoriesToFile(categories);
        return true;
    }

    @Override
    public List<Category> findAll() {
        return getAllCategories();
    }

    @Override
    public Optional<Category> findById(UUID id) {
        return getAllCategories().stream().filter(temp -> temp.getId().equals(id)).findFirst();
    }

    @NonNull
    private List<Category> getAllCategories() {
        List<Category> load = FileHelper.load(FIleURLS.CATEGORIES, new TypeToken<List<Category>>() {
        }.getType());
        return load == null ? new ArrayList<>() : load;
    }

    private void setAllCategoriesToFile(List<Category> data) {
        FileHelper.write(FIleURLS.CATEGORIES, data);
    }
}
