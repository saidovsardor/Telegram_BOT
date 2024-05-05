package uz.pdp.bot.service;

import lombok.Getter;
import uz.pdp.backend.model.User;
import uz.pdp.backend.repository.impl.AuthRepositoryImpl;
import uz.pdp.backend.utils.GlobalVar;
import uz.pdp.backend.utils.MessageKey;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class I18nService {

    @Getter
    private static final I18nService instance = new I18nService();
    private static final AuthRepositoryImpl authRepository = AuthRepositoryImpl.getInstance();
    private I18nService() {}

    public String getMsg(MessageKey key) {
        try {
            ResourceBundle resourceBundle = ResourceBundle.getBundle(
                    "properties/messages",
                    Locale.forLanguageTag(GlobalVar.getUSER().getLanguage().getCallbackData())
            );
            return resourceBundle.getString(key.getKey());
        }catch (Throwable e) {}
        return key.getVal();
    }

    public String getMsg(MessageKey key, Long userId) {
        try {
            User user = authRepository.findById(userId).get();
            ResourceBundle resourceBundle = ResourceBundle.getBundle(
                    "properties/messages",
                    Locale.forLanguageTag(user.getLanguage().getCallbackData())
            );
            return resourceBundle.getString(key.getKey());
        }catch (Throwable e) {}
        return key.getVal();
    }
}
