package uz.pdp.bot.service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.pdp.backend.enums.BusinessType;
import uz.pdp.backend.enums.Language;
import uz.pdp.backend.exceptions.WrongRoleException;
import uz.pdp.backend.model.Business;
import uz.pdp.backend.model.BusinessApplication;
import uz.pdp.backend.model.Category;
import uz.pdp.backend.service.*;
import uz.pdp.backend.utils.GlobalVar;
import uz.pdp.backend.utils.MessageKey;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ButtonService {
    public static final I18nService i18nService = I18nService.getInstance();
    public static final BusinessApplicationService businessAppService = BusinessApplicationService.getInstance();
    public static final CategoryService categoryService = CategoryService.getInstance();
    public static final BusinessService businessService = BusinessService.getInstance();
    public static final ProductService productService = ProductService.getInstance();
    public static ReplyKeyboard createLanguageButton(){
        Language[] values = Language.values();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (Language value : values) {
            buttons.add(List.of(InlineKeyboardButton.builder()
                            .text(value.getButtonText())
                            .callbackData(value.getCallbackData())
                    .build()));
        }
        return new InlineKeyboardMarkup(buttons);
    }

    public static ReplyKeyboard createSharePhoneNumber() {
        KeyboardButton build = KeyboardButton.builder()
                .text(i18nService.getMsg(MessageKey.SHARE_PHONE_NUMBER_BUTTON))
                .requestContact(true)
                .build();
        KeyboardRow keyboardButtons = new KeyboardRow(List.of(build));
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(List.of(keyboardButtons));
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboard createClientMainMenu() {
        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        // row 1
        keyboardRow.add(i18nService.getMsg(MessageKey.ORDER_BUTTON));
        keyboardRows.add(keyboardRow);

        // row 2
        keyboardRow = new KeyboardRow();
        if(!checkUserActiveApplication(GlobalVar.getUSER().getId())) {
            keyboardRow.add(i18nService.getMsg(MessageKey.SUBMIT_APPLICATION_BUTTON));
        }
        keyboardRow.add(i18nService.getMsg(MessageKey.SETTINGS_BUTTON));
        keyboardRows.add(keyboardRow);

        // -----

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboard createClientSettingsButtons() {
        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        // row 1
        keyboardRow.add(i18nService.getMsg(MessageKey.SET_LANGUAGE_BUTTON));
        keyboardRows.add(keyboardRow);
        // row 2
        keyboardRow = new KeyboardRow();
        keyboardRow.add(i18nService.getMsg(MessageKey.BACK));
        keyboardRows.add(keyboardRow);
        // -----

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboard createAdminPanelButton() {
        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        // row 1
        keyboardRow.add(i18nService.getMsg(MessageKey.GET_ALL_APPLICATION_BUTTON));
        keyboardRows.add(keyboardRow);

        keyboardRow = new KeyboardRow();
        keyboardRow.add(i18nService.getMsg(MessageKey.GET_REPORT_BUTTON));
        keyboardRows.add(keyboardRow);


        keyboardRow = new KeyboardRow();
        keyboardRow.add(i18nService.getMsg(MessageKey.SETTINGS_BUTTON));
        keyboardRows.add(keyboardRow);

        // row 2
//        keyboardRow = new KeyboardRow();
//        keyboardRow.add(i18nService.getMsg(MessageKey.BUSINESS_APPLICATION_BUTTON));
//        keyboardRow.add(i18nService.getMsg(MessageKey.SETTINGS_BUTTON));
//        keyboardRows.add(keyboardRow);

        // -----

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboard createAdminSettingsButton() {
        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();

        keyboardRow.add(i18nService.getMsg(MessageKey.SET_LANGUAGE_BUTTON));
        keyboardRows.add(keyboardRow);

        keyboardRow = new KeyboardRow();
        keyboardRow.add(i18nService.getMsg(MessageKey.CHANGE_CONTACT_BUTTON));
        keyboardRows.add(keyboardRow);

        keyboardRow = new KeyboardRow();
        keyboardRow.add(i18nService.getMsg(MessageKey.BACK));
        keyboardRows.add(keyboardRow);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboard createAdminAddContact() {
        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();

        KeyboardButton keyboardButton = new KeyboardButton(i18nService.getMsg(MessageKey.SHARE_PHONE_NUMBER_BUTTON));
        keyboardButton.setRequestContact(true);

        keyboardRow.add(keyboardButton);
        keyboardRows.add(keyboardRow);

        keyboardRow = new KeyboardRow();
        keyboardRow.add(i18nService.getMsg(MessageKey.BACK));
        keyboardRows.add(keyboardRow);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboard createAdminBusinessApplicationsButton() throws WrongRoleException {
        Long id = GlobalVar.getUSER().getId();
        List<BusinessApplication> applications = businessAppService.getAllActiveBusinessApplication(id);

        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow;

        for (BusinessApplication application : applications) {
            keyboardRow = new KeyboardRow();
            keyboardRow.add(application.getBusinessName() + " | " + application.getBusinessType());
            keyboardRows.add(keyboardRow);
        }

        keyboardRow = new KeyboardRow();
        keyboardRow.add(i18nService.getMsg(MessageKey.BACK));
        keyboardRows.add(keyboardRow);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }
    public static ReplyKeyboard createBusinessTypeButton() {
        BusinessType[] values = BusinessType.values();
        List<KeyboardRow> buttons = new ArrayList<>();
        for (BusinessType value : values) {
            buttons.add(new KeyboardRow(List.of(new KeyboardButton(value.getButtonName()))));
        }

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(buttons);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboard createShareLocation() {
        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();

        KeyboardButton keyboardButton = new KeyboardButton(i18nService.getMsg(MessageKey.SHARE_LOCATION_BUTTON));
        keyboardButton.setRequestLocation(true);
        keyboardRow.add(keyboardButton);
        keyboardRows.add(keyboardRow);

        keyboardRow = new KeyboardRow();
        keyboardRow.add(i18nService.getMsg(MessageKey.BACK));
        keyboardRows.add(keyboardRow);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboard createOpenBusinessApplicationButton() {

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        InlineKeyboardButton button1 = InlineKeyboardButton.builder()
                .text(i18nService.getMsg(MessageKey.ACCEPT_BUSINESS_APPLICATION))
                .callbackData(MessageKey.ACCEPT_BUSINESS_APPLICATION.getKey())
                .build();

        InlineKeyboardButton button2 = InlineKeyboardButton.builder()
                .text(i18nService.getMsg(MessageKey.REJECT_BUSINESS_APPLICATION))
                .callbackData(MessageKey.REJECT_BUSINESS_APPLICATION.getKey())
                .build();

        InlineKeyboardButton button3 = InlineKeyboardButton.builder()
                .text(i18nService.getMsg(MessageKey.BACK))
                .callbackData(MessageKey.BACK.getKey())
                .build();

        buttons.add(List.of(button1, button2));
        buttons.add(List.of(button3));
        return new InlineKeyboardMarkup(buttons);
    }

    public static ReplyKeyboard createBusinessmanPanelButtons() {
        ArrayList<KeyboardRow> buttons = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();

        // row 1
        keyboardRow.add(i18nService.getMsg(MessageKey.ADD_PRODUCT_BUTTON));
        buttons.add(keyboardRow);

        keyboardRow = new KeyboardRow();
        keyboardRow.add(i18nService.getMsg(MessageKey.EDIT_INFORMATION_BUTTON));
        buttons.add(keyboardRow);

        keyboardRow = new KeyboardRow();
        keyboardRow.add(i18nService.getMsg(MessageKey.SETTINGS_BUTTON));
        buttons.add(keyboardRow);




        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(buttons);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboard createBusinessmanSettingsButton() {
        ArrayList<KeyboardRow> buttons = new ArrayList<>();
        KeyboardRow keyboardButtons = new KeyboardRow();
        keyboardButtons.add(i18nService.getMsg(MessageKey.SET_LANGUAGE_BUTTON));

        buttons.add(keyboardButtons);

        keyboardButtons = new KeyboardRow();
        keyboardButtons.add(i18nService.getMsg(MessageKey.BACK));
        buttons.add(keyboardButtons);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(buttons);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboard createAdminApplicationTypeButton() {
        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();

        keyboardRow.add(i18nService.getMsg(MessageKey.BUSINESS_TYPE_APPLICATION_BUTTON));
        keyboardRows.add(keyboardRow);

        keyboardRow = new KeyboardRow();
        keyboardRow.add(i18nService.getMsg(MessageKey.COURIER_TYPE_APPLICATION_BUTTON));
        keyboardRows.add(keyboardRow);

        keyboardRow = new KeyboardRow();
        keyboardRow.add(i18nService.getMsg(MessageKey.BACK));
        keyboardRows.add(keyboardRow);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboard createAdminCourierApplicationsButton() {
        return null;
    }

    public static ReplyKeyboard createBackButton() {
        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();

        keyboardRow.add(i18nService.getMsg(MessageKey.BACK));
        keyboardRows.add(keyboardRow);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }

    private static boolean checkUserActiveApplication(Long userId){
        return businessAppService.checkMyActiveApplication(userId) || CourierApplicationService.getInstance().checkMyActiveApplication(userId);
    }

    public static ReplyKeyboard createClientApplicationTypeButton() {
        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();

        keyboardRow.add(i18nService.getMsg(MessageKey.BUSINESS_APPLICATION_BUTTON));
        keyboardRows.add(keyboardRow);
        keyboardRow = new KeyboardRow();
        keyboardRow.add(i18nService.getMsg(MessageKey.COURIER_APPLICATION_BUTTON));
        keyboardRows.add(keyboardRow);
        keyboardRow = new KeyboardRow();
        keyboardRow.add(i18nService.getMsg(MessageKey.BACK));
        keyboardRows.add(keyboardRow);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboard createClientOldAgeButton() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        InlineKeyboardButton button1 = InlineKeyboardButton.builder()
                .text(GlobalVar.getUSER().getBirthDate().format(DateTimeFormatter.ISO_DATE))
                .callbackData(GlobalVar.getUSER().getBirthDate().format(DateTimeFormatter.ISO_DATE))
                .build();

        buttons.add(List.of(button1));
        return new InlineKeyboardMarkup(buttons);
    }

    public static ReplyKeyboard createCategoriesButton(UUID businessId) {
        List<Category> categories = categoryService.getAllCategoriesOfBusiness(businessId);
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (Category category : categories) {
            buttons.add(List.of(
                    InlineKeyboardButton.builder()
                            .text(category.getCategoryName())
                            .callbackData(category.getId().toString())
                            .build()
            ));
        }
        buttons.add(List.of(
                InlineKeyboardButton.builder()
                        .text(i18nService.getMsg(MessageKey.BACK))
                        .callbackData(i18nService.getMsg(MessageKey.BACK))
                        .build()
        ));
        return new InlineKeyboardMarkup(buttons);
    }

    public static ReplyKeyboard createClientOrderButton() {
        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();

        keyboardRow.add(i18nService.getMsg(MessageKey.ADD_TO_BASKET_BUTTON));
        keyboardRows.add(keyboardRow);
        keyboardRow = new KeyboardRow();
        keyboardRow.add(i18nService.getMsg(MessageKey.OPEN_BASKET_BUTTON));
        keyboardRows.add(keyboardRow);
        keyboardRow = new KeyboardRow();
        keyboardRow.add(i18nService.getMsg(MessageKey.BACK));
        keyboardRows.add(keyboardRow);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboard createClientAllBusinessButton() {
        List<Business> allBusinesses = businessService.getAllBusinesses();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        Integer productsByBusiness;
        for (Business business : allBusinesses) {
            productsByBusiness = productService.countAllProductsByBusiness(business.getId());
            if(productsByBusiness != 0) {
                buttons.add(List.of(
                        InlineKeyboardButton.builder()
                                .text(business.getBusinessName() + " [" + productsByBusiness + "]")
                                .callbackData(business.getId().toString())
                                .build()
                ));
            }
        }
        buttons.add(List.of(
                InlineKeyboardButton.builder()
                        .text(i18nService.getMsg(MessageKey.BACK))
                        .callbackData(i18nService.getMsg(MessageKey.BACK))
                        .build()
        ));
        return new InlineKeyboardMarkup(buttons);
    }

    public static ReplyKeyboard createClientAllBusinessCategoryButton(UUID businessId) {
        List<Category> categories = categoryService.getAllCategoriesOfBusiness(businessId);
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        int productsByCategory;

        for (Category category : categories) {
            productsByCategory = productService.getCategoriesAllProducts(category.getId()).size();
            if(productsByCategory != 0) {
                buttons.add(List.of(
                        InlineKeyboardButton.builder()
                                .text(category.getCategoryName() + " [" + productsByCategory + "]")
                                .callbackData(category.getId().toString())
                                .build()
                ));
            }
        }
        buttons.add(List.of(
                InlineKeyboardButton.builder()
                        .text(i18nService.getMsg(MessageKey.BACK))
                        .callbackData(i18nService.getMsg(MessageKey.BACK))
                        .build()
        ));

        return new InlineKeyboardMarkup(buttons);

    }

    public static ReplyKeyboard createTypeOfEditingButton() {
        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();

        keyboardRow.add(i18nService.getMsg(MessageKey.EDIT_BUSINESS_NAME_BUTTON));
        keyboardRows.add(keyboardRow);

        keyboardRow = new KeyboardRow();
        keyboardRow.add(i18nService.getMsg(MessageKey.EDIT_BUSINESS_LOCATION_BUTTON));
        keyboardRows.add(keyboardRow);

        keyboardRow = new KeyboardRow();
        keyboardRow.add(i18nService.getMsg(MessageKey.BACK));
        keyboardRows.add(keyboardRow);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }
}
