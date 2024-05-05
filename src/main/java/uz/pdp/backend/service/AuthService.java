package uz.pdp.backend.service;

import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.Chat;
import uz.pdp.backend.enums.Role;
import uz.pdp.backend.enums.Status;
import uz.pdp.backend.model.User;
import uz.pdp.backend.repository.impl.AuthRepositoryImpl;
import uz.pdp.backend.utils.Bots;
import uz.pdp.bot.utils.InformationFormat;

import java.util.Optional;

public class AuthService {
    @Getter
    private static final AuthService instance = new AuthService();
    private static final AuthRepositoryImpl authRepository = AuthRepositoryImpl.getInstance();
    private AuthService() {}

    public User userVerify(Chat chat){
        Optional<User> user = authRepository.findById(chat.getId());
        if(user.isEmpty()){
            User newUser = User.builder()
                    .id(chat.getId())
//                    .role(Bots.ADMINS.contains(chat.getId()) ? Role.ADMIN:Role.CLIENT)
                    .role(Role.CLIENT)
                    .status(Status.STARTED)
//                    .status(Status.STARTED)
                    .fullName(InformationFormat.getFullName(chat.getFirstName(), chat.getLastName()))
                    .username(chat.getUserName())
                    .build();
            authRepository.save(newUser);
            return newUser;
        }
        return user.get();
    }
}
