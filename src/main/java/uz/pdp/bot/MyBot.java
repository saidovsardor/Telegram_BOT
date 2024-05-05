package uz.pdp.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.pdp.backend.utils.Bots;
import uz.pdp.bot.service.BotService;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyBot extends TelegramLongPollingBot {
    private static final ExecutorService executor = Executors.newCachedThreadPool();
    private final BotService botService = new BotService(this);

    {
        for (Long admin : Bots.ADMINS) {
            try {
                execute(new SendMessage(admin.toString(), "Bot ishga tushdi"));
            } catch (TelegramApiException ignored) {}
        }
    }

    public MyBot(String botToken) {
        super(botToken);
    }

    @Override
    public void onUpdateReceived(Update update) {
        executor.execute(() -> {
            botService.onUpdateReceived(update);
        });
    }

    @Override
    public String getBotUsername() {
        return Bots.USER_NAME;
    }
}
