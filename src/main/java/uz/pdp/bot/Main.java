package uz.pdp.bot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import uz.pdp.backend.enums.BusinessType;
import uz.pdp.backend.exceptions.ApplicationNotFoundException;
import uz.pdp.backend.exceptions.BusinessNotFoundException;
import uz.pdp.backend.exceptions.UserNotFoundException;
import uz.pdp.backend.exceptions.WrongRoleException;
import uz.pdp.backend.model.Business;
import uz.pdp.backend.model.BusinessApplication;
import uz.pdp.backend.payload.CreateBusinessApplicationDTO;
import uz.pdp.backend.payload.CreateProductDTO;
import uz.pdp.backend.repository.impl.CategoryRepositoryImpl;
import uz.pdp.backend.service.*;
import uz.pdp.backend.utils.Bots;
import uz.pdp.backend.utils.GlobalVar;
import uz.pdp.bot.service.BotService;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Main {
    private static final CategoryService categoryService = CategoryService.getInstance();
    private static final BusinessService businessService = BusinessService.getInstance();
    private static final ProductService productService = ProductService.getInstance();
    private static final CategoryRepositoryImpl categoryRepository = CategoryRepositoryImpl.getInstance();
    private static final AuthService authService = AuthService.getInstance();
    public static void main(String[] args) throws BusinessNotFoundException, WrongRoleException {

//        GlobalVar.log(Level.INFO, "asd", new RuntimeException());

        MyBot myBot = new MyBot(Bots.TOKEN);

        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(myBot);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }


//        categoryService.createCategory(UUID.fromString("3a964875-9435-43bb-8273-022053945ef6"), "Lavash");
//        System.out.println(categoryService.getAllCategoriesOfBusiness(UUID.fromString("3a964875-9435-43bb-8273-022053945ef6")));
//
//
//        CreateProductDTO createProductDTO = new CreateProductDTO("Spicy Lavash", 12L, "Tasty", null, 10, UUID.fromString("6413a694-f835-45b6-a2d5-c0f1b655048b"));
//        System.out.println(productService.createProduct(createProductDTO));
//        CreateProductDTO createProductDTO2 = new CreateProductDTO("Cheese Lavash", 10L, "Tasty", null, 12, UUID.fromString("6413a694-f835-45b6-a2d5-c0f1b655048b"));
//        System.out.println(productService.createProduct(createProductDTO2));
//
//        System.out.println(productService.getCategoriesAllProducts(UUID.fromString("6413a694-f835-45b6-a2d5-c0f1b655048b")));
    }
}