package uz.pdp.backend.utils;

import com.google.gson.*;
import uz.pdp.backend.model.BusinessApplication;
import uz.pdp.backend.model.User;
import uz.pdp.bot.MyBot;
import uz.pdp.bot.SimpleLoggingExample;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class GlobalVar {
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (localDateTime, type, jsonSerializationContext) -> {
                String format = localDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
                return new JsonPrimitive(format);
            })
            .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) -> {
                String asString = jsonElement.getAsJsonPrimitive().getAsString();
                return LocalDateTime.parse(asString);
            })
            .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (localDate, type, jsonSerializationContext) -> {
                String format = localDate.format(DateTimeFormatter.ISO_DATE);
                return new JsonPrimitive(format);
            })
            .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (jsonElement, type, jsonDeserializationContext) -> {
                String asString = jsonElement.getAsJsonPrimitive().getAsString();
                return LocalDate.parse(asString);
            })
            .create();

    private static final Logger logger = Logger.getLogger("FrontLogger");

    private static final ThreadLocal<User> USER = ThreadLocal.withInitial(() -> null);

    private static final ThreadLocal<MyBot> MY_BOT = ThreadLocal.withInitial(() -> null);
    private static final ThreadLocal<BusinessApplication> BUSINESS_APPLICATION = ThreadLocal.withInitial(() -> null);

    public static void log(Level level, String msg){
        logger.log(new LogRecord(level, "\n\n" + msg));
    }

    public static void log(Level level, String msg, Throwable exception){
        logger.log(level, "\n\n" + msg, exception);
    }

    public static void setUSER(User user) {
        USER.set(user);
    }

    public static User getUSER() {
        return USER.get();
    }

    public static void setMyBot(MyBot myBot) {
        MY_BOT.set(myBot);
    }

    public static MyBot getMyBot() {
        return MY_BOT.get();
    }
}
