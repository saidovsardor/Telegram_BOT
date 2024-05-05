package uz.pdp.backend.repository.impl;

import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.NonNull;
import uz.pdp.backend.model.Business;
import uz.pdp.backend.model.BusinessApplication;
import uz.pdp.backend.repository.BaseRepository;
import uz.pdp.backend.service.FileHelper;
import uz.pdp.backend.utils.FIleURLS;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class BusinessRepositoryImpl implements BaseRepository<Business, UUID> {
    @Getter
    private static final BusinessRepositoryImpl instance = new BusinessRepositoryImpl();

    private BusinessRepositoryImpl() {
    }

    @Override
    public Boolean save(Business business) {
        List<Business> allBusinesses = getAllBusinesses();
        allBusinesses.add(business);
        setAllBusinessesToFile(allBusinesses);
        return true;
    }

    @Override
    public Boolean update(Business business) {
        List<Business> businesses = getAllBusinesses();
        List<Business> updatedBusinesses = businesses.stream().map(
            temp -> {
                if (temp.getId().equals(business.getId()))
                    return business;
                return temp;
            }
        ).collect(Collectors.toList());

        setAllBusinessesToFile(updatedBusinesses);
        return true;
    }

    @Override
    public List<Business> findAll() {
        return getAllBusinesses();
    }

    @Override
    public Optional<Business> findById(UUID id) {
        List<Business> businesses = getAllBusinesses();
        return businesses.stream().filter(temp -> temp.getId().equals(id)).findFirst();
    }

    @NonNull
    private List<Business> getAllBusinesses() {
        List<Business> load = FileHelper.load(FIleURLS.BUSINESSES, new TypeToken<List<Business>>() {
        }.getType());
        return load == null ? new ArrayList<>() : load;
    }

    private void setAllBusinessesToFile(List<Business> data) {
        FileHelper.write(FIleURLS.BUSINESSES, data);
    }
}
