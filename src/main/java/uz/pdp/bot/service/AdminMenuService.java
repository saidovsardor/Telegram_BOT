package uz.pdp.bot.service;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.pdp.backend.enums.Language;
import uz.pdp.backend.enums.Role;
import uz.pdp.backend.enums.Status;
import uz.pdp.backend.exceptions.ApplicationNotFoundException;
import uz.pdp.backend.exceptions.BusinessNotFoundException;
import uz.pdp.backend.exceptions.UserNotFoundException;
import uz.pdp.backend.exceptions.WrongRoleException;
import uz.pdp.backend.model.Business;
import uz.pdp.backend.model.BusinessApplication;
import uz.pdp.backend.model.User;
import uz.pdp.backend.payload.RejectApplicationDTO;
import uz.pdp.backend.repository.impl.AuthRepositoryImpl;
import uz.pdp.backend.service.AuthService;
import uz.pdp.backend.service.BusinessApplicationService;
import uz.pdp.backend.service.BusinessService;
import uz.pdp.backend.utils.GlobalVar;
import uz.pdp.backend.utils.MessageKey;
import uz.pdp.bot.model.TempData;
import uz.pdp.bot.repository.TempDataRepository;
import uz.pdp.bot.utils.TempDataKeys;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static uz.pdp.backend.utils.CheckUserInformation.checkPhoneNumber;
import static uz.pdp.bot.service.ResponseService.*;
import static uz.pdp.backend.utils.MessageKey.*;
import static uz.pdp.bot.utils.TempDataKeys.*;

public class AdminMenuService {
    private static final AuthRepositoryImpl authRepository = AuthRepositoryImpl.getInstance();
    private static final AuthService authService = AuthService.getInstance();
    private static final I18nService i18nService = I18nService.getInstance();
    private static final BusinessApplicationService businessAppService = BusinessApplicationService.getInstance();
    private static final BusinessService businessService = BusinessService.getInstance();
    private static final TempDataRepository tempDataRepository = TempDataRepository.getInstance();

    public static void started(Update update, Message message) {
        User user = GlobalVar.getUSER();
        user.setStatus(Status.CHOOSE_LANGUAGE);
        authRepository.update(user);
        sendMessage(message.getChatId(), i18nService.getMsg(STARTED_ADMIN).formatted(message.getChat().getFirstName()), ButtonService.createLanguageButton());
    }

    public static void chooseLanguage(Update update, Message message) {

        if (!update.hasCallbackQuery()) {
            sendMessage(message.getChatId(), i18nService.getMsg(INFORMATION_ERROR));
            return;
        }

        User user = GlobalVar.getUSER();
        findLanguage(message.getText(), user);
        user.setStatus(Status.MAIN_MENU);
        authRepository.update(user);
        editMessage(message.getChatId(), message.getMessageId(), i18nService.getMsg(STARTED_ADMIN).formatted(message.getChat().getFirstName()));
        sendMessage(message.getChatId(), ADMIN_PANEL, ButtonService.createAdminPanelButton());
    }

    public static void mainMenu(Update update, Message message) {
        User user = GlobalVar.getUSER();
        if (message.getText().equals(i18nService.getMsg(SETTINGS_BUTTON))) {
            user.setStatus(Status.SETTINGS);
            sendMessage(message.getChatId(), SETTINGS_ADMIN, ButtonService.createAdminSettingsButton());
        } else if (message.getText().equals(i18nService.getMsg(GET_ALL_APPLICATION_BUTTON))) {
            sendMessage(message.getChatId(), APPLICATIONS_TYPE_ADMIN, ButtonService.createAdminApplicationTypeButton());
            user.setStatus(Status.CHOOSE_APPLICATION_TYPE_MENU);
//            try {
//                sendMessage(message.getChatId(), APPLICATIONS_TYPE_ADMIN, ButtonService.createAdminApplicationTypeButton());
//                user.setStatus(Status.CHOOSE_APPLICATION_TYPE_MENU);
//            } catch (WrongRoleException e) {
//                GlobalVar.log(Level.SEVERE, "You are not admin!!!", e);
//                user.setStatus(Status.MAIN_MENU);
//                sendMessage(message.getChatId(), WRONG_ROLE_ADMIN, ButtonService.createAdminPanelButton());
//            }
        } else if (message.getText().equals(i18nService.getMsg(GET_REPORT_BUTTON))) {
            user.setStatus(Status.CHECK_REPORTS);
            authRepository.update(user);
            List<User> allUsers = authRepository.findAll().stream().filter(temp -> !temp.isDelete()).collect(Collectors.toList());
            int countOfUsers = allUsers.size();

            List<User> couriers = authRepository.findAll().stream().filter(temp -> temp.getRole().equals(Role.COURIER) && !temp.isDelete()).collect(Collectors.toList());
            int countOfCouriers = couriers.size();

            List<Business> allBusinesses = businessService.getAllBusinesses();
            int countOfBusinesses = allBusinesses.size();

            sendMessage(message.getChatId(), i18nService.getMsg(REPORTS_ADMIN).formatted(
                    countOfUsers,
                    countOfCouriers,
                    countOfBusinesses
            ), ButtonService.createBackButton());
        } else {
            sendMessage(message.getChatId(), INFORMATION_ERROR);
            return;
        }
        authRepository.update(user);
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

    public static void settingsMenu(Update update, Message message) {
        User user = GlobalVar.getUSER();
        if (message.getText().equals(i18nService.getMsg(SET_LANGUAGE_BUTTON))) {
            user.setStatus(Status.SET_LANGUAGE);
            sendMessage(message.getChatId(), SET_LANGUAGE, ButtonService.createLanguageButton());
        } else if (message.getText().equals(i18nService.getMsg(CHANGE_CONTACT_BUTTON))) {
            user.setStatus(Status.SET_PHONE_NUMBER);
            sendMessage(message.getChatId(), ADD_CONTACT_ADMIN, ButtonService.createAdminAddContact());
        } else if (message.getText().equals(i18nService.getMsg(BACK))) {
            user.setStatus(Status.MAIN_MENU);
            sendMessage(message.getChatId(), ADMIN_PANEL, ButtonService.createAdminPanelButton());
        } else {
            sendMessage(message.getChatId(), INFORMATION_ERROR);
            return;
        }
        authRepository.update(user);
    }

    public static void setLanguage(Update update, Message message) {
        User user = GlobalVar.getUSER();
        if (!update.hasCallbackQuery()) {
            sendMessage(message.getChatId(), i18nService.getMsg(INFORMATION_ERROR));
            return;
        }
        findLanguage(message.getText(), user);
        user.setStatus(Status.SETTINGS);
        authRepository.update(user);
        editMessage(message.getChatId(), message.getMessageId(), SET_LANGUAGE);
        sendMessage(message.getChatId(), SETTINGS_ADMIN, ButtonService.createAdminSettingsButton());
    }

    public static void setPhoneNumber(Update update, Message message) {
        User user = GlobalVar.getUSER();

        String phoneNumber;
        if (message.hasContact()) {
            phoneNumber = message.getContact().getPhoneNumber();
        } else {
            if (message.getText().equals(i18nService.getMsg(BACK))) {
                user.setStatus(Status.SETTINGS);
                authRepository.update(user);
                sendMessage(message.getChatId(), SETTINGS_ADMIN, ButtonService.createAdminSettingsButton());
                return;
            }
            phoneNumber = message.getText();
        }

        if (!checkPhoneNumber(phoneNumber)) {
            sendMessage(message.getChatId(), i18nService.getMsg(INFORMATION_ERROR));
            return;
        }

        user.setStatus(Status.SETTINGS);
        user.setPhoneNumber(phoneNumber);
        authRepository.update(user);
        sendMessage(message.getChatId(), SETTINGS_ADMIN, ButtonService.createAdminSettingsButton());
    }

    public static void checkApplications(Update update, Message message) {
        User user = GlobalVar.getUSER();
        Long id = user.getId();

        if (message.getText().equals(i18nService.getMsg(BACK))) {
            user.setStatus(Status.CHOOSE_APPLICATION_TYPE_MENU);
            authRepository.update(user);
            sendMessage(message.getChatId(), APPLICATIONS_TYPE_ADMIN, ButtonService.createAdminApplicationTypeButton());
            return;
        }

        List<BusinessApplication> applications;
        try {
            applications = businessAppService.getAllActiveBusinessApplication(id);
        } catch (WrongRoleException e) {
            GlobalVar.log(Level.SEVERE, "You are not admin!!!", e);
            user.setStatus(Status.MAIN_MENU);
            sendMessage(message.getChatId(), WRONG_ROLE_ADMIN, ButtonService.createAdminPanelButton());
            return;
        }

        Optional<BusinessApplication> firstBusiness = applications.stream()
                .filter(temp -> message.getText().equals(temp.getBusinessName() + " | " + temp.getBusinessType()))
                .findFirst();

        if (firstBusiness.isEmpty()) {
            sendMessage(message.getChatId(), i18nService.getMsg(INFORMATION_ERROR));
            return;
        }

        BusinessApplication businessApplication = firstBusiness.get();

        tempDataRepository.createData(new TempData<>(BUSINESS_APPLICATION, firstBusiness.get()));

        user.setStatus(Status.OPEN_BUSINESS_APPLICATION);
        authRepository.update(user);
        User owner = authRepository.findById(businessApplication.getOwnerId()).get();
        sendMessage(message.getChatId(), i18nService.getMsg(OPEN_BUSINESS_APPLICATION).formatted(
                owner.getFullName(),
                owner.getUsername(),
                owner.getPhoneNumber(),
                businessApplication.getBusinessName(),
                businessApplication.getBusinessType(),
                businessApplication.getCreateTime().format(DateTimeFormatter.ISO_DATE_TIME)
        ), ButtonService.createOpenBusinessApplicationButton());
    }

    public static void openBusinessApplication(Update update, Message message) {
        User user = GlobalVar.getUSER();
        if (message.getText().equals(ACCEPT_BUSINESS_APPLICATION.getKey())) {
            BusinessApplication application = (BusinessApplication) tempDataRepository.getAndDelete(BUSINESS_APPLICATION).getVal();
            try {
                Optional<User> applicationOwner = authRepository.findById(application.getOwnerId());
                if (applicationOwner.isEmpty()) {
                    businessAppService.rejectBusinessApplication(application.getId(), null);
                    return;
                }
                businessAppService.acceptBusinessApplication(application.getId());
                applicationOwner = authRepository.findById(application.getOwnerId());
                User owner = applicationOwner.get();
                owner.setStatus(Status.MAIN_MENU);
                authRepository.update(owner);
                sendMessage(application.getOwnerId(), i18nService.getMsg(ACCEPTED_APPLICATION_ADMIN, application.getOwnerId()), ButtonService.createBusinessmanPanelButtons());
            } catch (ApplicationNotFoundException e) {
                GlobalVar.log(Level.WARNING, "Application not found!", e);
            } catch (BusinessNotFoundException e) {
                GlobalVar.log(Level.SEVERE, "Business not found can not created Business location!!!", e);
            }
            try {
                sendMessage(message.getChatId(), APPLICATIONS_ADMIN, ButtonService.createAdminBusinessApplicationsButton());
                user.setStatus(Status.CHECK_BUSINESS_APPLICATION_MENU);
            } catch (WrongRoleException e) {
                GlobalVar.log(Level.SEVERE, "You are not admin!!!", e);
                user.setStatus(Status.MAIN_MENU);
                sendMessage(message.getChatId(), WRONG_ROLE_ADMIN, ButtonService.createAdminPanelButton());
            }

        } else if (message.getText().equals(REJECT_BUSINESS_APPLICATION.getKey())) {
            user.setStatus(Status.WRITING_COMMENT_MENU);
            sendMessage(message.getChatId(), i18nService.getMsg(WRITE_COMMENT_ADMIN));
        } else if (message.getText().equals(BACK.getKey())) {
            try {
                sendMessage(message.getChatId(), APPLICATIONS_ADMIN, ButtonService.createAdminBusinessApplicationsButton());
                user.setStatus(Status.CHECK_BUSINESS_APPLICATION_MENU);
            } catch (WrongRoleException e) {
                GlobalVar.log(Level.SEVERE, "You are not admin!!!", e);
                user.setStatus(Status.MAIN_MENU);
                sendMessage(message.getChatId(), WRONG_ROLE_ADMIN, ButtonService.createAdminPanelButton());
            }
        } else {
            sendMessage(message.getChatId(), MAIN_MENU, ButtonService.createAdminPanelButton());
            return;
        }
        deleteMessage(message.getChatId(), message.getMessageId());
        authRepository.update(user);
    }

    public static void writingCommentMenu(Update update, Message message) {
        User user = GlobalVar.getUSER();
        BusinessApplication application = (BusinessApplication) tempDataRepository.getAndDelete(BUSINESS_APPLICATION).getVal();
        try {
            RejectApplicationDTO rejectApplicationDTO = businessAppService.rejectBusinessApplication(application.getId(), message.getText());
            sendMessage(application.getOwnerId(), i18nService.getMsg(REJECTED_APPLICATION_ADMIN, application.getOwnerId()) + "-" + rejectApplicationDTO.getRejectReason());

        } catch (ApplicationNotFoundException e) {
            GlobalVar.log(Level.WARNING, "Application not found!", e);
        }
        try {
            sendMessage(message.getChatId(), APPLICATIONS_ADMIN, ButtonService.createAdminBusinessApplicationsButton());
            user.setStatus(Status.CHECK_BUSINESS_APPLICATION_MENU);
        } catch (WrongRoleException e) {
            GlobalVar.log(Level.SEVERE, "You are not admin!!!", e);
            user.setStatus(Status.MAIN_MENU);
            sendMessage(message.getChatId(), WRONG_ROLE_ADMIN, ButtonService.createAdminPanelButton());
        }
        authRepository.update(user);
    }

    public static void choosingApplicationTypeMenu(Update update, Message message) {
        User user = GlobalVar.getUSER();

        if (message.getText().equals(i18nService.getMsg(BACK))) {
            user.setStatus(Status.MAIN_MENU);
            authRepository.update(user);
            sendMessage(message.getChatId(), MAIN_MENU, ButtonService.createAdminPanelButton());
            return;
        } else if (message.getText().equals(i18nService.getMsg(BUSINESS_TYPE_APPLICATION_BUTTON))) {
            user.setStatus(Status.CHECK_BUSINESS_APPLICATION_MENU);
            authRepository.update(user);
            try {
                sendMessage(message.getChatId(), APPLICATIONS_ADMIN, ButtonService.createAdminBusinessApplicationsButton());
            } catch (WrongRoleException e) {
                GlobalVar.log(Level.SEVERE, "You are not admin!!!", e);
                user.setStatus(Status.MAIN_MENU);
                sendMessage(message.getChatId(), WRONG_ROLE_ADMIN, ButtonService.createAdminPanelButton());
            }
        } else if (message.getText().equals(i18nService.getMsg(COURIER_TYPE_APPLICATION_BUTTON))) {
            user.setStatus(Status.CHOOSE_APPLICATION_TYPE_MENU);
//            user.setStatus(Status.CHECK_COURIER_APPLICATION_MENU);
            authRepository.update(user);
            sendMessage(message.getChatId(), APPLICATIONS_ADMIN, ButtonService.createAdminCourierApplicationsButton());
        }
    }

    public static void checkReportsMenu(Update update, Message message) {
        User user = GlobalVar.getUSER();

        if (message.getText().equals(i18nService.getMsg(BACK))) {
            user.setStatus(Status.MAIN_MENU);
            authRepository.update(user);
            sendMessage(message.getChatId(), MAIN_MENU, ButtonService.createAdminPanelButton());
            return;
        }
    }
}
