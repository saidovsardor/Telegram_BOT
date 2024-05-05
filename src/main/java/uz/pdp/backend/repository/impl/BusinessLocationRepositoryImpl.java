package uz.pdp.backend.repository.impl;

import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.NonNull;
import uz.pdp.backend.model.BusinessLocation;
import uz.pdp.backend.repository.BaseRepository;
import uz.pdp.backend.service.FileHelper;
import uz.pdp.backend.utils.FIleURLS;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class BusinessLocationRepositoryImpl implements BaseRepository<BusinessLocation, UUID> {
    @Getter
    private static final BusinessLocationRepositoryImpl instance = new BusinessLocationRepositoryImpl();
    private BusinessLocationRepositoryImpl(){}
    @Override
    public Boolean save(BusinessLocation businessLocation) {
        List<BusinessLocation> businessLocations = getAllBusinessLocationsFromFile();
        businessLocations.add(businessLocation);
        setAllBusinessLocationsToFile(businessLocations);
        return true;
    }

    @Override
    public Boolean update(BusinessLocation businessLocation) {
        List<BusinessLocation> businessLocations = getAllBusinessLocationsFromFile();
        businessLocations.stream().map(
                temp -> {
                    if(temp.getId().equals(businessLocation.getId()))
                        return businessLocation;
                    return temp;
                }
        ).collect(Collectors.toList());

        setAllBusinessLocationsToFile(businessLocations);
        return true;
    }

    @Override
    public List<BusinessLocation> findAll() {
        return getAllBusinessLocationsFromFile();
    }

    @Override
    public Optional<BusinessLocation> findById(UUID id) {
        return getAllBusinessLocationsFromFile().stream().filter(temp -> temp.getId().equals(id)).findFirst();
    }

    @NonNull
    private List<BusinessLocation> getAllBusinessLocationsFromFile(){
        List<BusinessLocation> load = FileHelper.load(FIleURLS.BUSINESS_LOCATIONS, new TypeToken<List<BusinessLocation>>() {}.getType());
        return load == null ? new ArrayList<>() : load;
    }

    private void setAllBusinessLocationsToFile(List<BusinessLocation> data){
        FileHelper.write(FIleURLS.BUSINESS_LOCATIONS, data);
    }
}
