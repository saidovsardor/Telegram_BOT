package uz.pdp.bot.service;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.pdp.backend.enums.Role;
import uz.pdp.backend.enums.Status;
import uz.pdp.backend.exceptions.UserNotFoundException;
import uz.pdp.backend.exceptions.WrongRoleException;
import uz.pdp.backend.model.User;
import uz.pdp.backend.repository.impl.AuthRepositoryImpl;
import uz.pdp.backend.service.AuthService;
import uz.pdp.backend.utils.GlobalVar;
import uz.pdp.bot.MyBot;
import uz.pdp.bot.model.TempData;
import uz.pdp.bot.repository.TempDataRepository;

import java.util.ArrayList;

import static uz.pdp.bot.service.ResponseService.sendMessage;


public class BotService {
    private MyBot bot;
    private final AuthService authService = AuthService.getInstance();
    private final AuthRepositoryImpl authRepository = AuthRepositoryImpl.getInstance();
    private final TempDataRepository tempDataRepository = TempDataRepository.getInstance();

    public BotService(MyBot bot) {
        this.bot = bot;
    }



    public void onUpdateReceived(Update update){
        GlobalVar.setMyBot(bot);
        Message message;
        if(update.hasMessage()){
            message = update.getMessage();
        } else if(update.hasCallbackQuery()){
            message = update.getCallbackQuery().getMessage();
            message.setText(update.getCallbackQuery().getData());
        } else
            return;
        User user = authService.userVerify(message.getChat());
        GlobalVar.setUSER(user);
        if(user.getRole() == Role.CLIENT) clientCases(update, message, user.getStatus());
        else if(user.getRole() == Role.ADMIN) adminCases(update, message, user.getStatus());
        else if(user.getRole() == Role.BUSINESSMAN) businessmanCases(update, message, user.getStatus());
    }

    private void businessmanCases(Update update, Message message, Status status) {
        switch (status){
            case STARTED -> {
                sendMessage(message.getChatId(), "mainMenu", ButtonService.createBusinessmanPanelButtons());
                User user = GlobalVar.getUSER();
                user.setStatus(Status.MAIN_MENU);
                authRepository.update(user);
            }
            case MAIN_MENU -> BusinessmanMenuService.mainMenu(update, message);
            case SETTINGS -> BusinessmanMenuService.settingsMenu(update, message);
            case SET_LANGUAGE -> BusinessmanMenuService.setLanguage(update, message);
            case ADD_PRODUCT_BUSINESSMAN -> BusinessmanMenuService.addProduct(update, message);
            case CHOOSE_TYPE_OF_EDITING -> BusinessmanMenuService.chooseTypeOfEditing(update, message);
            case EDIT_BUSINESS_NAME -> BusinessmanMenuService.editBusinessName(update, message);
            case EDIT_BUSINESS_LOCATION -> BusinessmanMenuService.editBusinessLocation(update, message);
        }
    }

    public void clientCases(Update update, Message message, Status userStatus){
        switch (userStatus){
            case STARTED -> RegistrationService.started(update, message);
            case CHOOSE_LANGUAGE -> RegistrationService.chooseLanguage(update, message);
            case SHARE_PHONE_NUMBER -> RegistrationService.sharePhoneNumber(update, message);
            case MAIN_MENU -> ClientMenuService.mainMenu(update, message);
            case SETTINGS -> ClientMenuService.settingsMenu(update, message);
            case SET_LANGUAGE -> ClientMenuService.setLanguage(update, message);
            case CHOOSE_APPLICATION_TYPE_MENU -> ClientMenuService.chooseApplicationType(update, message);
            case BUSINESS_APPLICATION_MENU -> ClientMenuService.businessApplicationMenu(update, message);
            case COURIER_APPLICATION_MENU -> ClientMenuService.courierApplicationMenu(update, message);
            case ORDER_MENU -> ClientMenuService.orderMenu(update, message);
            case CHOOSE_BUSINESS -> ClientMenuService.chooseBusiness(update, message);
            case CHOOSE_CATEGORY -> ClientMenuService.chooseCategory(update, message);

        }
    }
    public void adminCases(Update update, Message message, Status userStatus){
        switch (userStatus){
            case STARTED -> AdminMenuService.started(update, message);
            case CHOOSE_LANGUAGE -> AdminMenuService.chooseLanguage(update, message);
            case MAIN_MENU -> AdminMenuService.mainMenu(update, message);
            case SETTINGS -> AdminMenuService.settingsMenu(update, message);
            case SET_LANGUAGE -> AdminMenuService.setLanguage(update, message);
            case SET_PHONE_NUMBER -> AdminMenuService.setPhoneNumber(update, message);
            case CHECK_BUSINESS_APPLICATION_MENU -> AdminMenuService.checkApplications(update, message);
            case OPEN_BUSINESS_APPLICATION -> AdminMenuService.openBusinessApplication(update, message);
            case WRITING_COMMENT_MENU -> AdminMenuService.writingCommentMenu(update, message);
            case CHOOSE_APPLICATION_TYPE_MENU -> AdminMenuService.choosingApplicationTypeMenu(update, message);
            case CHECK_REPORTS -> AdminMenuService.checkReportsMenu(update, message);
        }
    }
}
