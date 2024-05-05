package uz.pdp.backend.repository.impl;

import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.NonNull;
import uz.pdp.backend.model.BusinessApplication;
import uz.pdp.backend.model.User;
import uz.pdp.backend.repository.BaseRepository;
import uz.pdp.backend.service.FileHelper;
import uz.pdp.backend.utils.FIleURLS;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class BusinessApplicationRepositoryImpl implements BaseRepository<BusinessApplication, UUID> {
    @Getter
    private static final BusinessApplicationRepositoryImpl instance = new BusinessApplicationRepositoryImpl();
    private BusinessApplicationRepositoryImpl(){}
    @Override
    public Boolean save(BusinessApplication businessApplication) {
        List<BusinessApplication> allBusinessApplications = getAllBusinessApplicationFromFile();
        allBusinessApplications.add(businessApplication);
        setAllBusinessApplicationsToFile(allBusinessApplications);
        return true;
    }

    @Override
    public Boolean update(BusinessApplication businessApplication) {
        List<BusinessApplication> allBusinessApplications = getAllBusinessApplicationFromFile();
        List<BusinessApplication> updatedAllBusinessApplications = allBusinessApplications.stream().map(
                temp -> {
                    if (temp.getId().equals(businessApplication.getId()))
                        return businessApplication;
                    return temp;
                }
        ).collect(Collectors.toList());

        setAllBusinessApplicationsToFile(updatedAllBusinessApplications);
        return true;
    }

    @Override
    public List<BusinessApplication> findAll() {
        return getAllBusinessApplicationFromFile();
    }

    @Override
    public Optional<BusinessApplication> findById(UUID id) {
        return getAllBusinessApplicationFromFile().stream().filter(application -> application.getId().equals(id)).findFirst();
    }

    @NonNull
    private List<BusinessApplication> getAllBusinessApplicationFromFile(){
        List<BusinessApplication> load = FileHelper.load(FIleURLS.BUSINESS_APPLICATIONS, new TypeToken<List<BusinessApplication>>() {}.getType());
        return load == null ? new ArrayList<>() : load;
    }

    private void setAllBusinessApplicationsToFile(List<BusinessApplication> data){
        FileHelper.write(FIleURLS.BUSINESS_APPLICATIONS, data);
    }
}
