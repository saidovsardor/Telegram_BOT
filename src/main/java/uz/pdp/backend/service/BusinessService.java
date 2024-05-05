package uz.pdp.backend.service;

import lombok.Getter;
import uz.pdp.backend.enums.BusinessType;
import uz.pdp.backend.enums.Role;
import uz.pdp.backend.exceptions.BusinessNotFoundException;
import uz.pdp.backend.exceptions.WrongRoleException;
import uz.pdp.backend.model.Business;
import uz.pdp.backend.model.User;
import uz.pdp.backend.repository.impl.AuthRepositoryImpl;
import uz.pdp.backend.repository.impl.BusinessRepositoryImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BusinessService {
    @Getter
    private static final BusinessService instance = new BusinessService();
    private static final BusinessRepositoryImpl businessRepository = BusinessRepositoryImpl.getInstance();
    private static final AuthRepositoryImpl authRepository = AuthRepositoryImpl.getInstance();
    private BusinessService() {}
    public Business createBusiness(Long ownerId, String businessName, BusinessType businessType){
        Business business = new Business(ownerId, businessName, businessType);
        businessRepository.save(business);
        return business;
    }

    public List<Business> getAllBusinesses() {
        List<Business> businesses = businessRepository.findAll();
        return businesses.stream().filter(temp -> !temp.isDelete()).collect(Collectors.toList());
    }

    public Business getMyBusiness(Long id) throws BusinessNotFoundException {
        User user = authRepository.findById(id).get();

        List<Business> businesses = businessRepository.findAll();
        Optional<Business> optionalBusiness = businesses.stream().filter(temp -> !temp.isDelete() && temp.getOwnerId().equals(id)).findFirst();
        if(optionalBusiness.isEmpty())
            throw new BusinessNotFoundException("You do not have business");

        return optionalBusiness.get();
    }

}
