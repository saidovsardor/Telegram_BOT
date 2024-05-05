package uz.pdp.bot.service;

import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.pdp.backend.enums.BusinessType;
import uz.pdp.backend.enums.Language;
import uz.pdp.backend.enums.Status;
import uz.pdp.backend.exceptions.UserNotFoundException;
import uz.pdp.backend.model.Business;
import uz.pdp.backend.model.User;
import uz.pdp.backend.payload.CreateBusinessApplicationDTO;
import uz.pdp.backend.repository.impl.AuthRepositoryImpl;
import uz.pdp.backend.service.BusinessApplicationService;
import uz.pdp.backend.service.BusinessService;
import uz.pdp.backend.service.CourierApplicationService;
import uz.pdp.backend.utils.GlobalVar;
import uz.pdp.backend.utils.MessageKey;
import uz.pdp.backend.utils.Regexes;
import uz.pdp.bot.model.TempData;
import uz.pdp.bot.repository.TempDataRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.regex.Pattern;

import static uz.pdp.backend.utils.MessageKey.*;
import static uz.pdp.bot.service.ResponseService.*;
import static uz.pdp.bot.utils.TempDataKeys.*;

public class ClientMenuService {
    private static final AuthRepositoryImpl authRepository = AuthRepositoryImpl.getInstance();
    private static final I18nService i18nService = I18nService.getInstance();
    private static final TempDataRepository tempDataRepository = TempDataRepository.getInstance();
    private static final BusinessApplicationService businessAppService = BusinessApplicationService.getInstance();
    private static final CourierApplicationService courierAppService = CourierApplicationService.getInstance();
    private static final BusinessService businessService = BusinessService.getInstance();

    public static void mainMenu(Update update, Message message) {
        User user = GlobalVar.getUSER();
        if(message.getText().equals(i18nService.getMsg(SETTINGS_BUTTON))) {
            user.setStatus(Status.SETTINGS);
            sendMessage(message.getChatId(), SETTINGS, ButtonService.createClientSettingsButtons());
        } else if(message.getText().equals(i18nService.getMsg(ORDER_BUTTON))) {
            user.setStatus(Status.ORDER_MENU);
            sendMessage(message.getChatId(), MessageKey.ORDER_MENU, ButtonService.createClientOrderButton());
        } else if(!checkUserActiveApplication(user.getId()) && message.getText().equals(i18nService.getMsg(SUBMIT_APPLICATION_BUTTON))) {
            user.setStatus(Status.CHOOSE_APPLICATION_TYPE_MENU);
            sendMessage(message.getChatId(), CHOOSE_APPLICATION_TYPE, ButtonService.createClientApplicationTypeButton());
        } else {
            sendMessage(message.getChatId(), INFORMATION_ERROR);
            return;
        }
        authRepository.update(user);
    }

    public static void settingsMenu(Update update, Message message) {
        User user = GlobalVar.getUSER();
        if(message.getText().equals(i18nService.getMsg(SET_LANGUAGE_BUTTON))) {
            user.setStatus(Status.SET_LANGUAGE);
            sendMessage(message.getChatId(), SET_LANGUAGE, ButtonService.createLanguageButton());
        } else if(message.getText().equals(i18nService.getMsg(BACK))){
            user.setStatus(Status.MAIN_MENU);
            sendMessage(message.getChatId(), MAIN_MENU, ButtonService.createClientMainMenu());
        } else {
            sendMessage(message.getChatId(), INFORMATION_ERROR);
            return;
        }
        authRepository.update(user);
    }

    public static void setLanguage(Update update, Message message) {
        if (!update.hasCallbackQuery()) {
            sendMessage(message.getChatId(), i18nService.getMsg(INFORMATION_ERROR));
            return;
        }

        User user = GlobalVar.getUSER();
        findLanguage(message.getText(), user);
        authRepository.update(user);


        user.setStatus(Status.SETTINGS);
        authRepository.update(user);
        editMessage(message.getChatId(), message.getMessageId(), SET_LANGUAGE);
        sendMessage(message.getChatId(), SETTINGS, ButtonService.createClientSettingsButtons());
    }

    public static void businessApplicationMenu(Update update, Message message) {
        if (!tempDataRepository.contains(BUSINESS_NAME)){
            tempDataRepository.createData(new TempData<>(BUSINESS_NAME, message.getText()));
            sendMessage(message.getChatId(), BUSINESS_APPLICATION_ENTER_TYPE, ButtonService.createBusinessTypeButton());
        } else if(!tempDataRepository.contains(BUSINESS_TYPE)){
            Optional<BusinessType> businessType = findBusinessType(message.getText());
            if(businessType.isEmpty()){
                sendMessage(message.getChatId(), INFORMATION_ERROR);
                return;
            }
            tempDataRepository.createData(new TempData<>(BUSINESS_TYPE, businessType.get()));
            sendMessage(message.getChatId(), BUSINESS_APPLICATION_ENTER_LOCATION, ButtonService.createShareLocation());
        } else if(!tempDataRepository.contains(BUSINESS_LOCATION)){
            if(!message.hasLocation()){
                sendMessage(message.getChatId(), INFORMATION_ERROR);
                return;
            }
            tempDataRepository.createData(new TempData<>(BUSINESS_LOCATION, message.getLocation()));

            User user = GlobalVar.getUSER();
            try {
                CreateBusinessApplicationDTO createBusinessApplicationDTO = CreateBusinessApplicationDTO.builder()
                        .businessName((String) tempDataRepository.getAndDelete(BUSINESS_NAME).getVal())
                        .businessType((BusinessType) tempDataRepository.getAndDelete(BUSINESS_TYPE).getVal())
                        .location((Location) tempDataRepository.getAndDelete(BUSINESS_LOCATION).getVal())
                        .ownerId(user.getId())
                        .build();

                businessAppService.createBusinessApplication(createBusinessApplicationDTO);

                user.setStatus(Status.MAIN_MENU);
                authRepository.update(user);
                sendMessage(message.getChatId(), FINISHED_BUSINESS_APPLICATION, ButtonService.createClientMainMenu());
            }catch (ClassCastException e){
                GlobalVar.log(Level.WARNING, "Class cast error", e);
                user.setStatus(Status.MAIN_MENU);
                tempDataRepository.deleteMyAllData();
                authRepository.update(user);
                sendMessage(message.getChatId(), MAIN_MENU, ButtonService.createClientMainMenu());
            }

        }
    }

    private static Optional<BusinessType> findBusinessType(String msg) {
        BusinessType[] values = BusinessType.values();
        for (BusinessType value : values) {
            if(value.getButtonName().equals(msg)){
                return Optional.of(value);
            }
        }
        return Optional.empty();
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

    private static boolean checkUserActiveApplication(Long userId){
        return businessAppService.checkMyActiveApplication(userId) || CourierApplicationService.getInstance().checkMyActiveApplication(userId);
    }

    public static void chooseApplicationType(Update update, Message message) {
        User user = GlobalVar.getUSER();
        if(message.getText().equals(i18nService.getMsg(BUSINESS_APPLICATION_BUTTON))){
            user.setStatus(Status.BUSINESS_APPLICATION_MENU);
            sendMessage(message.getChatId(), BUSINESS_APPLICATION_ENTER_NAME);
        } else if(message.getText().equals(i18nService.getMsg(COURIER_APPLICATION_BUTTON))){
            user.setStatus(Status.COURIER_APPLICATION_MENU);
            if(user.getBirthDate()==null)
                sendMessage(message.getChatId(), COURIER_APPLICATION_ENTER_BIRTH_DATE);
            else
                sendMessage(message.getChatId(), COURIER_APPLICATION_ENTER_BIRTH_DATE, ButtonService.createClientOldAgeButton());
        } else if (message.getText().equals(i18nService.getMsg(BACK))){
            user.setStatus(Status.MAIN_MENU);
            sendMessage(message.getChatId(), MAIN_MENU, ButtonService.createClientMainMenu());
        } else {
            sendMessage(message.getChatId(), INFORMATION_ERROR);
            return;
        }
        authRepository.update(user);
    }

    public static void courierApplicationMenu(Update update, Message message) {
        User user = GlobalVar.getUSER();
        LocalDate birthDate;
        if(user.getBirthDate()!=null && update.hasCallbackQuery()){
            if(message.getText().equals(GlobalVar.getUSER().getBirthDate().format(DateTimeFormatter.ISO_DATE))) {
                birthDate = user.getBirthDate();
                deleteMessage(message.getChatId(), message.getMessageId());
            } else {
                sendMessage(message.getChatId(), INFORMATION_ERROR);
                return;
            }
        } else if(Pattern.compile(Regexes.BIRTH_DATE).matcher(message.getText()).matches()){
            birthDate = LocalDate.parse(message.getText(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        } else {
            sendMessage(message.getChatId(), INFORMATION_ERROR);
            return;
        }
        user.setBirthDate(birthDate);
        authRepository.update(user);
        try {
            courierAppService.createApplication(user.getId());
        } catch (IllegalArgumentException e){
            user.setStatus(Status.CHOOSE_APPLICATION_TYPE_MENU);
            authRepository.update(user);
            sendMessage(message.getChatId(), "Yoshingiz to'g'ri kelmaydi!");
            sendMessage(message.getChatId(), CHOOSE_APPLICATION_TYPE, ButtonService.createClientApplicationTypeButton());
            return;
        } catch (UserNotFoundException e) {
            GlobalVar.log(Level.WARNING, e.getMessage(), e);
        }
        user.setStatus(Status.MAIN_MENU);
        authRepository.update(user);
        sendMessage(message.getChatId(), MAIN_MENU, ButtonService.createClientMainMenu());
    }

    public static void orderMenu(Update update, Message message) {
        User user = GlobalVar.getUSER();

        if(message.getText().equals(i18nService.getMsg(ADD_TO_BASKET_BUTTON))){
            user.setStatus(Status.CHOOSE_BUSINESS);
            sendMessage(message.getChatId(), MessageKey.CHOOSE_BUSINESS, ButtonService.createClientAllBusinessButton());
        } else if(message.getText().equals(i18nService.getMsg(OPEN_BASKET_BUTTON))){

        } else if(message.getText().equals(i18nService.getMsg(BACK))){
            user.setStatus(Status.MAIN_MENU);
            sendMessage(message.getChatId(), MAIN_MENU, ButtonService.createClientMainMenu());
        } else {
            sendMessage(message.getChatId(), INFORMATION_ERROR);
            return;
        }
        authRepository.update(user);
    }

    public static void chooseBusiness(Update update, Message message) {
        User user = GlobalVar.getUSER();

        if(!update.hasCallbackQuery()){
            sendMessage(message.getChatId(), INFORMATION_ERROR);
            return;
        }

        if(message.getText().equals(i18nService.getMsg(BACK))){
            user.setStatus(Status.ORDER_MENU);
            authRepository.update(user);
            sendMessage(message.getChatId(), ORDER_MENU, ButtonService.createClientOrderButton());
            return;
        }

        List<Business> allBusinesses = businessService.getAllBusinesses();

        for (Business business : allBusinesses) {
            if(business.getId().toString().equals(message.getText())){
                user.setStatus(Status.CHOOSE_CATEGORY);
                authRepository.update(user);
                tempDataRepository.createData(new TempData<>(CURRENT_BUSINESS_ID, business.getId()));
                editMessage(message.getChatId(), message.getMessageId(), MessageKey.CHOOSE_CATEGORY, ButtonService.createClientAllBusinessCategoryButton(business.getId()));
                return;
            }
        }
        sendMessage(message.getChatId(), INFORMATION_ERROR);
    }

    public static void chooseCategory(Update update, Message message) {
        User user = GlobalVar.getUSER();
        if(!update.hasCallbackQuery()){
            sendMessage(message.getChatId(), INFORMATION_ERROR);
            return;
        }

        if(message.getText().equals(i18nService.getMsg(BACK))){
            user.setStatus(Status.ORDER_MENU);
            authRepository.update(user);
            sendMessage(message.getChatId(), ORDER_MENU, ButtonService.createClientOrderButton());
            return;
        }

    }
}
