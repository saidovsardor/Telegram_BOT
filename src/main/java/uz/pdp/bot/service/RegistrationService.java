package uz.pdp.bot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.pdp.backend.enums.Language;
import uz.pdp.backend.enums.Status;
import uz.pdp.backend.model.User;
import uz.pdp.backend.repository.impl.AuthRepositoryImpl;
import uz.pdp.backend.utils.GlobalVar;

import java.util.logging.Level;

import static uz.pdp.backend.utils.MessageKey.*;
import static uz.pdp.backend.utils.CheckUserInformation.*;
import static uz.pdp.bot.service.ResponseService.*;

public class RegistrationService {
    private static final AuthRepositoryImpl authRepository = AuthRepositoryImpl.getInstance();
    private static final I18nService i18nService = I18nService.getInstance();

    public static void started(Update update, Message message) {
        User user = GlobalVar.getUSER();
        user.setStatus(Status.CHOOSE_LANGUAGE);
        authRepository.update(user);
        sendMessage(message.getChatId(), CHOOSE_LANGUAGE, ButtonService.createLanguageButton());
    }

    public static void chooseLanguage(Update update, Message message) {

        if (!update.hasCallbackQuery()) {
            sendMessage(message.getChatId(), i18nService.getMsg(INFORMATION_ERROR));
            return;
        }

        User user = GlobalVar.getUSER();
        findLanguage(message.getText(), user);


        user.setStatus(Status.SHARE_PHONE_NUMBER);
        editMessage(message.getChatId(), message.getMessageId(), CHOOSE_LANGUAGE);
        sendMessage(message.getChatId(), SHARE_PHONE_NUMBER, ButtonService.createSharePhoneNumber());
        authRepository.update(user);
    }

    public static void sharePhoneNumber(Update update, Message message) {
        String phoneNumber;
        if (message.hasContact()) {
            phoneNumber = message.getContact().getPhoneNumber();
        } else {
            phoneNumber = message.getText();
        }

        if(!phoneNumber.startsWith("+")) phoneNumber = "+" + phoneNumber;

        if (!checkPhoneNumber(phoneNumber)) {
            sendMessage(message.getChatId(), i18nService.getMsg(INFORMATION_ERROR));
            return;
        }

        User user = GlobalVar.getUSER();
        user.setStatus(Status.MAIN_MENU);
        user.setPhoneNumber(phoneNumber);
        authRepository.update(user);
        sendMessage(message.getChatId(), MAIN_MENU, ButtonService.createClientMainMenu());
    }

    private static void findLanguage(String text, User user) {
        Language[] values = Language.values();
        for (Language value : values) {
            if (value.getCallbackData().equals(text)) {
                user.setLanguage(value);
                return;
            }
        }
    }
}
