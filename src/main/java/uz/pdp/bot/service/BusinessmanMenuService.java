package uz.pdp.bot.service;

import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.pdp.backend.enums.Language;
import uz.pdp.backend.enums.Status;
import uz.pdp.backend.exceptions.BusinessNotFoundException;
import uz.pdp.backend.model.Business;
import uz.pdp.backend.model.BusinessLocation;
import uz.pdp.backend.model.Category;
import uz.pdp.backend.model.User;
import uz.pdp.backend.payload.CreateProductDTO;
import uz.pdp.backend.repository.impl.AuthRepositoryImpl;
import uz.pdp.backend.repository.impl.BusinessRepositoryImpl;
import uz.pdp.backend.service.BusinessLocationService;
import uz.pdp.backend.service.BusinessService;
import uz.pdp.backend.service.CategoryService;
import uz.pdp.backend.service.ProductService;
import uz.pdp.backend.utils.GlobalVar;
import uz.pdp.backend.utils.MessageKey;
import uz.pdp.bot.model.TempData;
import uz.pdp.bot.repository.TempDataRepository;
import uz.pdp.bot.utils.TempDataKeys;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;

import static uz.pdp.backend.utils.MessageKey.*;
import static uz.pdp.bot.service.ResponseService.*;

public class BusinessmanMenuService {
    private static final I18nService i18nService = I18nService.getInstance();
    private static final AuthRepositoryImpl authRepository = AuthRepositoryImpl.getInstance();
    private static final BusinessService businessService = BusinessService.getInstance();
    private static final BusinessRepositoryImpl businessRepository = BusinessRepositoryImpl.getInstance();
    private static final BusinessLocationService businessLocationService = BusinessLocationService.getInstance();
    private static final CategoryService categoryService = CategoryService.getInstance();
    private static final ProductService productService = ProductService.getInstance();
    private static final TempDataRepository tempDataRepository = TempDataRepository.getInstance();

    public static void mainMenu(Update update, Message message) {
        User user = GlobalVar.getUSER();
        if (message.getText().equals(i18nService.getMsg(ADD_PRODUCT_BUTTON))) {
            try {
                Business myBusiness = businessService.getMyBusiness(GlobalVar.getUSER().getId());
                tempDataRepository.createData(new TempData<>(TempDataKeys.CURRENT_BUSINESS_ID, myBusiness.getId()));
                user.setStatus(Status.ADD_PRODUCT_BUSINESSMAN);
                sendMessage(message.getChatId(), MessageKey.ADD_PRODUCT_ENTER_CATEGORY, ButtonService.createCategoriesButton(myBusiness.getId()));
            } catch (BusinessNotFoundException e) {
                GlobalVar.log(Level.WARNING, e.getMessage(), e);
                sendErrorMessage(message.getChatId());
                return;
            }
        } else if (message.getText().equals(i18nService.getMsg(MessageKey.SETTINGS_BUTTON))) {
            user.setStatus(Status.SETTINGS);
            sendMessage(message.getChatId(), MessageKey.SETTINGS_BUSINESSMAN, ButtonService.createBusinessmanSettingsButton());
        } else if (message.getText().equals(i18nService.getMsg(EDIT_INFORMATION_BUTTON))) {
            user.setStatus(Status.CHOOSE_TYPE_OF_EDITING);


            try {
                Business myBusiness = businessService.getMyBusiness(user.getId());

                List<BusinessLocation> locationsOfBusiness = businessLocationService.getLocationsOfBusiness(myBusiness.getId());

                int countOfLocations = locationsOfBusiness.size();

                sendMessage(message.getChatId(), i18nService.getMsg(CHOOSE_TYPE_OF_EDITING).formatted(
                        myBusiness.getBusinessName(),
                        myBusiness.getBusinessType(),
                        myBusiness.getBalanceCardNumber(),
                        countOfLocations
                ), ButtonService.createTypeOfEditingButton());
            } catch (BusinessNotFoundException e) {
                GlobalVar.log(Level.WARNING, e.getMessage(), e);
                sendErrorMessage(message.getChatId());
                return;
            }

        } else {
            sendMessage(message.getChatId(), MessageKey.INFORMATION_ERROR);
            return;
        }
        authRepository.update(user);


    }

    public static void settingsMenu(Update update, Message message) {
        User user = GlobalVar.getUSER();
        if (message.getText().equals(i18nService.getMsg(MessageKey.SET_LANGUAGE_BUTTON))) {
            user.setStatus(Status.SET_LANGUAGE);
            sendMessage(message.getChatId(), MessageKey.SET_LANGUAGE, ButtonService.createLanguageButton());
        } else if (message.getText().equals(i18nService.getMsg(MessageKey.BACK))) {
            user.setStatus(Status.MAIN_MENU);
            sendMessage(message.getChatId(), MessageKey.BUSINESSMAN_PANEL, ButtonService.createBusinessmanPanelButtons());
        } else {
            sendMessage(message.getChatId(), MessageKey.INFORMATION_ERROR);
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
        sendMessage(message.getChatId(), SETTINGS_BUSINESSMAN, ButtonService.createBusinessmanSettingsButton());
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

    public static void addProduct(Update update, Message message) {

        if (message.getText().equals(i18nService.getMsg(BACK))) {
            User user = GlobalVar.getUSER();
            if (!tempDataRepository.contains(TempDataKeys.ADD_PRODUCT_CATEGORY_NAME)) {
                tempDataRepository.deleteData(TempDataKeys.CURRENT_BUSINESS_ID);
                user.setStatus(Status.MAIN_MENU);
                deleteMessage(message.getChatId(), message.getMessageId());
                sendMessage(message.getChatId(), MessageKey.BUSINESSMAN_PANEL, ButtonService.createBusinessmanPanelButtons());
            } else if (!tempDataRepository.contains(TempDataKeys.ADD_PRODUCT_PRODUCT_NAME)) {

                UUID businessId = (UUID) tempDataRepository.get(TempDataKeys.CURRENT_BUSINESS_ID).getVal();
                tempDataRepository.deleteData(TempDataKeys.ADD_PRODUCT_CATEGORY_NAME);
                sendMessage(message.getChatId(), MessageKey.ADD_PRODUCT_ENTER_CATEGORY, ButtonService.createCategoriesButton(businessId));

            } else if (!tempDataRepository.contains(TempDataKeys.ADD_PRODUCT_PRODUCT_PRICE)) {

                tempDataRepository.deleteData(TempDataKeys.ADD_PRODUCT_PRODUCT_NAME);
                sendMessage(message.getChatId(), ADD_PRODUCT_ENTER_NAME, ButtonService.createBackButton());

            } else if (!tempDataRepository.contains(TempDataKeys.ADD_PRODUCT_PRODUCT_AMOUNT)) {

                tempDataRepository.deleteData(TempDataKeys.ADD_PRODUCT_PRODUCT_PRICE);
                sendMessage(message.getChatId(), ADD_PRODUCT_ENTER_PRICE, ButtonService.createBackButton());

            } else if (!tempDataRepository.contains(TempDataKeys.ADD_PRODUCT_PRODUCT_PHOTO)) {

                tempDataRepository.deleteData(TempDataKeys.ADD_PRODUCT_PRODUCT_AMOUNT);
                sendMessage(message.getChatId(), ADD_PRODUCT_ENTER_AMOUNT, ButtonService.createBackButton());

            } else if (!tempDataRepository.contains(TempDataKeys.ADD_PRODUCT_PRODUCT_DESCRIPTION)) {

                tempDataRepository.deleteData(TempDataKeys.ADD_PRODUCT_PRODUCT_PHOTO);
                sendMessage(message.getChatId(), ADD_PRODUCT_ENTER_PHOTO, ButtonService.createBackButton());

            }

            authRepository.update(user);
            return;
        }

        if (!tempDataRepository.contains(TempDataKeys.ADD_PRODUCT_CATEGORY_NAME)) {
            UUID businessId = (UUID) tempDataRepository.get(TempDataKeys.CURRENT_BUSINESS_ID).getVal();
            if (update.hasCallbackQuery()) {
                List<Category> categories = categoryService.getAllCategoriesOfBusiness(businessId);
                Optional<Category> category = categories.stream().filter(temp -> temp.getId().toString().equals(message.getText())).findFirst();
                if (category.isEmpty()) {
                    sendMessage(message.getChatId(), INFORMATION_ERROR);
                    return;
                }
                tempDataRepository.createData(new TempData<>(TempDataKeys.ADD_PRODUCT_CATEGORY_NAME, category.get().getCategoryName()));
            } else {
                tempDataRepository.createData(new TempData<>(TempDataKeys.ADD_PRODUCT_CATEGORY_NAME, message.getText()));
            }
            sendMessage(message.getChatId(), ADD_PRODUCT_ENTER_NAME, ButtonService.createBackButton());
        } else if (!tempDataRepository.contains(TempDataKeys.ADD_PRODUCT_PRODUCT_NAME)) {
            tempDataRepository.createData(new TempData<>(TempDataKeys.ADD_PRODUCT_PRODUCT_NAME, message.getText()));
            sendMessage(message.getChatId(), ADD_PRODUCT_ENTER_PRICE, ButtonService.createBackButton());
        } else if (!tempDataRepository.contains(TempDataKeys.ADD_PRODUCT_PRODUCT_PRICE)) {
            try {
                long price = Long.parseLong(message.getText());
                tempDataRepository.createData(new TempData<>(TempDataKeys.ADD_PRODUCT_PRODUCT_PRICE, price));
                sendMessage(message.getChatId(), ADD_PRODUCT_ENTER_AMOUNT, ButtonService.createBackButton());
            } catch (IllegalArgumentException e) {
                sendMessage(message.getChatId(), INFORMATION_ERROR);
                return;
            }
        } else if (!tempDataRepository.contains(TempDataKeys.ADD_PRODUCT_PRODUCT_AMOUNT)) {
            try {
                Integer amount = Integer.parseInt(message.getText());
                tempDataRepository.createData(new TempData<>(TempDataKeys.ADD_PRODUCT_PRODUCT_AMOUNT, amount));
                sendMessage(message.getChatId(), ADD_PRODUCT_ENTER_PHOTO, ButtonService.createBackButton());
            } catch (IllegalArgumentException e) {
                sendMessage(message.getChatId(), INFORMATION_ERROR);
                return;
            }
        } else if (!tempDataRepository.contains(TempDataKeys.ADD_PRODUCT_PRODUCT_PHOTO)) {
            tempDataRepository.createData(new TempData<String>(TempDataKeys.ADD_PRODUCT_PRODUCT_PHOTO, "URL"));
            sendMessage(message.getChatId(), ADD_PRODUCT_ENTER_DESCRIPTION, ButtonService.createBackButton());
        } else if (!tempDataRepository.contains(TempDataKeys.ADD_PRODUCT_PRODUCT_DESCRIPTION)) {
            tempDataRepository.createData(new TempData<>(TempDataKeys.ADD_PRODUCT_PRODUCT_DESCRIPTION, message.getText()));
            User user = GlobalVar.getUSER();
            try {
                CreateProductDTO createProductDTO = CreateProductDTO.builder()
                        .businessId((UUID) tempDataRepository.getAndDelete(TempDataKeys.CURRENT_BUSINESS_ID).getVal())
                        .categoryName((String) tempDataRepository.getAndDelete(TempDataKeys.ADD_PRODUCT_CATEGORY_NAME).getVal())
                        .productName((String) tempDataRepository.getAndDelete(TempDataKeys.ADD_PRODUCT_PRODUCT_NAME).getVal())
                        .price((Long) tempDataRepository.getAndDelete(TempDataKeys.ADD_PRODUCT_PRODUCT_PRICE).getVal())
                        .amount((int) tempDataRepository.getAndDelete(TempDataKeys.ADD_PRODUCT_PRODUCT_AMOUNT).getVal())
                        .photoUrl((String) tempDataRepository.getAndDelete(TempDataKeys.ADD_PRODUCT_PRODUCT_PHOTO).getVal())
                        .description((String) tempDataRepository.getAndDelete(TempDataKeys.ADD_PRODUCT_PRODUCT_DESCRIPTION).getVal())
                        .build();
                productService.createProduct(createProductDTO);
                user.setStatus(Status.MAIN_MENU);
                authRepository.update(user);
                sendMessage(message.getChatId(), ADD_PRODUCT_FINISHED, ButtonService.createBusinessmanPanelButtons());
            } catch (ClassCastException e) {
                GlobalVar.log(Level.WARNING, "Class cast error", e);
                user.setStatus(Status.MAIN_MENU);
                tempDataRepository.deleteMyAllData();
                authRepository.update(user);
                sendErrorMessage(message.getChatId());
                sendMessage(message.getChatId(), BUSINESSMAN_PANEL, ButtonService.createBusinessmanPanelButtons());
            }
        }
    }

    public static void chooseTypeOfEditing(Update update, Message message) {
        User user = GlobalVar.getUSER();

        if (message.getText().equals(i18nService.getMsg(EDIT_BUSINESS_NAME_BUTTON))) {
            user.setStatus(Status.EDIT_BUSINESS_NAME);
            sendMessage(message.getChatId(), ENTER_NAME_FOR_BUSINESS);
        } else if (message.getText().equals(i18nService.getMsg(EDIT_BUSINESS_LOCATION_BUTTON))) {
            user.setStatus(Status.EDIT_BUSINESS_LOCATION);
            sendMessage(message.getChatId(), ENTER_LOCATION_FOR_BUSINESS, ButtonService.createShareLocation());
        } else if (message.getText().equals(i18nService.getMsg(MessageKey.BACK))) {
            user.setStatus(Status.MAIN_MENU);
            sendMessage(message.getChatId(), MessageKey.BUSINESSMAN_PANEL, ButtonService.createBusinessmanPanelButtons());
        }
        authRepository.update(user);
    }

    public static void editBusinessName(Update update, Message message) {
        User user = GlobalVar.getUSER();

        try {
            Business myBusiness = businessService.getMyBusiness(user.getId());
            myBusiness.setBusinessName(message.getText());

            List<BusinessLocation> locationsOfBusiness = businessLocationService.getLocationsOfBusiness(myBusiness.getId());

            int countOfLocations = locationsOfBusiness.size();

            businessRepository.update(myBusiness);

            sendMessage(message.getChatId(), BUSINESS_NAME_EDITED);

            sendMessage(message.getChatId(), i18nService.getMsg(CHOOSE_TYPE_OF_EDITING).formatted(
                    myBusiness.getBusinessName(),
                    myBusiness.getBusinessType(),
                    myBusiness.getBalanceCardNumber(),
                    countOfLocations
            ), ButtonService.createTypeOfEditingButton());
        } catch (BusinessNotFoundException e) {
            GlobalVar.log(Level.WARNING, e.getMessage(), e);
            sendErrorMessage(message.getChatId());
            return;
        }

        user.setStatus(Status.CHOOSE_TYPE_OF_EDITING);
        authRepository.update(user);
    }

    public static void editBusinessLocation(Update update, Message message) {
        User user = GlobalVar.getUSER();


        try {
            Business myBusiness = businessService.getMyBusiness(user.getId());

            List<BusinessLocation> locationsOfBusiness = businessLocationService.getLocationsOfBusiness(myBusiness.getId());

            int countOfLocations = locationsOfBusiness.size();

            if (message.getText()!=null && message.getText().equals(i18nService.getMsg(MessageKey.BACK))) {
                user.setStatus(Status.CHOOSE_TYPE_OF_EDITING);
                sendMessage(message.getChatId(), i18nService.getMsg(CHOOSE_TYPE_OF_EDITING).formatted(
                        myBusiness.getBusinessName(),
                        myBusiness.getBusinessType(),
                        myBusiness.getBalanceCardNumber(),
                        countOfLocations
                ), ButtonService.createTypeOfEditingButton());
                authRepository.update(user);
                return;
            } else if (!message.hasLocation()) {
                sendMessage(message.getChatId(), INFORMATION_ERROR);
                return;
            }

            Location location = message.getLocation();

            businessLocationService.createBusinessLocation(myBusiness.getId(), location);

            sendMessage(message.getChatId(), i18nService.getMsg(CHOOSE_TYPE_OF_EDITING).formatted(
                    myBusiness.getBusinessName(),
                    myBusiness.getBusinessType(),
                    myBusiness.getBalanceCardNumber(),
                    countOfLocations
            ), ButtonService.createTypeOfEditingButton());
        } catch (BusinessNotFoundException e) {
            GlobalVar.log(Level.WARNING, e.getMessage(), e);
            sendErrorMessage(message.getChatId());
            return;
        }
        user.setStatus(Status.CHOOSE_TYPE_OF_EDITING);
        authRepository.update(user);
    }
}
