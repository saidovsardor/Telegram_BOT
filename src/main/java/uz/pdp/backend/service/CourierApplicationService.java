package uz.pdp.backend.service;
import lombok.Getter;
import uz.pdp.backend.enums.Role;
import uz.pdp.backend.exceptions.ApplicationNotFoundException;
import uz.pdp.backend.exceptions.UserNotFoundException;
import uz.pdp.backend.exceptions.WrongRoleException;
import uz.pdp.backend.model.CourierApplication;
import uz.pdp.backend.model.User;
import uz.pdp.backend.payloa.CourierRejectApplicationDTO;
import uz.pdp.backend.repository.impl.AuthRepositoryImpl;
import uz.pdp.backend.repository.impl.CourierApplicationRepositoryImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class CourierApplicationService {
    @Getter
    private static final CourierApplicationService instance = new CourierApplicationService();
    private static final CourierApplicationRepositoryImpl courierRepository = CourierApplicationRepositoryImpl.getInstance();
    private static final AuthRepositoryImpl authRepository = AuthRepositoryImpl.getInstance();
    private CourierApplicationService() {
    }
    public CourierApplication createApplication (long ownerId) throws UserNotFoundException {
        Optional<User> user = authRepository.findById(ownerId);
        if (user.isEmpty()){
            throw new UserNotFoundException("User not Found!");
        }

        if (user.get().getBirthDate()==null || Period.between(user.get().getBirthDate(), LocalDate.now()).getYears()<18){
            throw new IllegalArgumentException("Age is not valid!");
        }
        CourierApplication newCourier = new CourierApplication(ownerId, LocalDateTime.now(), true);
        courierRepository.save(newCourier);
        return newCourier;
    }
    public List<CourierApplication> getAllActiveApplication(long userId) throws WrongRoleException {
        Optional<User> user = authRepository.findById(userId);
        if (user.get().getRole()!= Role.ADMIN){
            throw new WrongRoleException("You are not admin!");
        }
        List<CourierApplication> collect = courierRepository.findAll().stream().filter(CourierApplication::isActive).collect(Collectors.toList());
        return collect;
    }
    public CourierRejectApplicationDTO rejectCourierApplication(long applicationId, String rejectReason) throws ApplicationNotFoundException {
        Optional<CourierApplication> applicationOptional = courierRepository.findById(applicationId);
        if(applicationOptional.isEmpty()) {
            throw new ApplicationNotFoundException("Application not found");
        }
        CourierApplication application = applicationOptional.get();
        application.setActive(false);
        courierRepository.update(application);
       return new CourierRejectApplicationDTO(application,rejectReason);
    }
    public User acceptCourierApplication(long applicationId) throws ApplicationNotFoundException, UserNotFoundException {
        Optional<CourierApplication> applicationOptional = courierRepository.findById(applicationId);
        if(applicationOptional.isEmpty())
            throw new ApplicationNotFoundException("Application not found");
        CourierApplication application = applicationOptional.get();
        application.setActive(false);
        courierRepository.update(application);

        User user = authRepository.findById(application.getOwnerId()).get();
        user.setRole(Role.COURIER);
        authRepository.update(user);
        return user;
    }


    public boolean checkMyActiveApplication(Long userId) {
        List<CourierApplication> all = courierRepository.findAll();
        return all.stream().anyMatch(temp -> Objects.equals(temp.getOwnerId(), userId) && temp.isActive());
    }
}

