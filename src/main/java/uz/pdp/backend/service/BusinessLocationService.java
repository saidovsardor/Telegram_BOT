package uz.pdp.backend.service;

import lombok.Getter;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.objects.Location;
import uz.pdp.backend.exceptions.BusinessNotFoundException;
import uz.pdp.backend.model.Business;
import uz.pdp.backend.model.BusinessLocation;
import uz.pdp.backend.repository.impl.BusinessLocationRepositoryImpl;
import uz.pdp.backend.repository.impl.BusinessRepositoryImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class BusinessLocationService {
    private static final BusinessLocationRepositoryImpl businessLocationRepository = BusinessLocationRepositoryImpl.getInstance();
    private static final BusinessRepositoryImpl businessRepository = BusinessRepositoryImpl.getInstance();
    @Getter
    private static final BusinessLocationService instance = new BusinessLocationService();
    private BusinessLocationService(){}

    public void createBusinessLocation(UUID businessId, Location location) throws BusinessNotFoundException {
        Optional<Business> optionalBusiness = businessRepository.findById(businessId);
        if(optionalBusiness.isEmpty())
            throw new BusinessNotFoundException("Business not found!");

        BusinessLocation businessLocation = new BusinessLocation(location, businessId);
        businessLocationRepository.save(businessLocation);
    }

    public List<BusinessLocation> getLocationsOfBusiness(UUID businessId) throws BusinessNotFoundException {
        Optional<Business> optionalBusiness = businessRepository.findById(businessId);
        if(optionalBusiness.isEmpty())
            throw new BusinessNotFoundException("Business not found!");

        List<BusinessLocation> businessLocations = businessLocationRepository.findAll();
        return businessLocations.stream().filter(temp -> temp.getBusinessId().equals(businessId) && !temp.isDelete()).collect(Collectors.toList());
    }
}
