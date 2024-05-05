package uz.pdp.backend.service;

import lombok.Getter;
import uz.pdp.backend.enums.Role;
import uz.pdp.backend.exceptions.ApplicationNotFoundException;
import uz.pdp.backend.exceptions.BusinessNotFoundException;
import uz.pdp.backend.exceptions.UserNotFoundException;
import uz.pdp.backend.exceptions.WrongRoleException;
import uz.pdp.backend.model.Business;
import uz.pdp.backend.model.BusinessApplication;
import uz.pdp.backend.model.User;
import uz.pdp.backend.payload.CreateBusinessApplicationDTO;
import uz.pdp.backend.payload.RejectApplicationDTO;
import uz.pdp.backend.repository.impl.AuthRepositoryImpl;
import uz.pdp.backend.repository.impl.BusinessApplicationRepositoryImpl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BusinessApplicationService {
    @Getter
    private static final BusinessApplicationService instance = new BusinessApplicationService();
    private static final BusinessService businessService = BusinessService.getInstance();
    private static final AuthRepositoryImpl authRepository = AuthRepositoryImpl.getInstance();
    private static final BusinessLocationService businessLocationService = BusinessLocationService.getInstance();
    private static final BusinessApplicationRepositoryImpl businessApplicationRepository = BusinessApplicationRepositoryImpl.getInstance();
    private BusinessApplicationService(){}
    public BusinessApplication createBusinessApplication(CreateBusinessApplicationDTO dto){
        BusinessApplication application = new BusinessApplication(dto.ownerId(), dto.businessName(), dto.businessType(), dto.location());
        businessApplicationRepository.save(application);
        return application;
    }

    public User acceptBusinessApplication(UUID applicationId) throws ApplicationNotFoundException, BusinessNotFoundException {
        Optional<BusinessApplication> applicationOptional = findApplication(temp -> temp.getId().equals(applicationId));
        if(applicationOptional.isEmpty())
            throw new ApplicationNotFoundException("Application not found");

        BusinessApplication application = applicationOptional.get();
        application.setActive(false);
        businessApplicationRepository.update(application);

        Business business = businessService.createBusiness(application.getOwnerId(), application.getBusinessName(), application.getBusinessType());
        businessLocationService.createBusinessLocation(business.getId(), application.getLocation());

        User user = authRepository.findById(application.getOwnerId()).get();
        user.setRole(Role.BUSINESSMAN);
        authRepository.update(user);

        return user;
    }

    public RejectApplicationDTO rejectBusinessApplication(UUID applicationId, String rejectReason) throws ApplicationNotFoundException {
        Optional<BusinessApplication> applicationOptional = findApplication(temp -> temp.getId().equals(applicationId));
        if(applicationOptional.isEmpty())
            throw new ApplicationNotFoundException("Application not found");

        BusinessApplication application = applicationOptional.get();
        application.setActive(false);
        businessApplicationRepository.update(application);

        return new RejectApplicationDTO(application, rejectReason);
    }

    public List<BusinessApplication> getAllActiveBusinessApplication(Long userId) throws WrongRoleException {
        Optional<User> optionalUser = authRepository.findById(userId);

        User user = optionalUser.get();
        if(user.getRole() != Role.ADMIN)
            throw new WrongRoleException("You are not Admin!");

        return businessApplicationRepository.findAll().stream().filter(BusinessApplication::isActive).collect(Collectors.toList());
    }
    public boolean checkMyActiveApplication(Long ownerId){
        List<BusinessApplication> all = businessApplicationRepository.findAll();
        return all.stream().anyMatch(temp -> Objects.equals(temp.getOwnerId(), ownerId) && temp.isActive());
    }

    private Optional<BusinessApplication> findApplication(Predicate<BusinessApplication> predicate) {
        List<BusinessApplication> applications = businessApplicationRepository.findAll();
        return applications.stream().filter(predicate).findFirst();
    }
}
