package uz.pdp.bot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.pdp.backend.utils.GlobalVar;
import uz.pdp.backend.utils.MessageKey;

import java.util.logging.Level;

public class ResponseService {
    private static final I18nService i18nService = I18nService.getInstance();
    public static void sendMessage(Long chatId, String text, ReplyKeyboard replyKeyboard){
        try {
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(chatId.toString())
                    .text(text)
                    .replyMarkup(replyKeyboard)
                    .build();
            GlobalVar.getMyBot().execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            GlobalVar.log(Level.WARNING, "Can not send message", e);
        }
    }
    public static void sendMessage(Long chatId, MessageKey key, ReplyKeyboard replyKeyboard){
        try {
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(chatId.toString())
                    .text(i18nService.getMsg(key, chatId))
                    .replyMarkup(replyKeyboard)
                    .build();
            GlobalVar.getMyBot().execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            GlobalVar.log(Level.WARNING, "Can not send message", e);
        }
    }

    public static void sendMessage(Long chatId, String text){
        try {
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(chatId.toString())
                    .text(text)
                    .build();
            GlobalVar.getMyBot().execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            GlobalVar.log(Level.WARNING, "Can not send message", e);
        }
    }
    public static void sendMessage(Long chatId, MessageKey key){
        try {
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(chatId.toString())
                    .text(i18nService.getMsg(key, chatId))
                    .build();
            GlobalVar.getMyBot().execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            GlobalVar.log(Level.WARNING, "Can not send message", e);
        }
    }

    public static void sendErrorMessage(Long chatId){
        try {
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(chatId.toString())
                    .text("ERROR")
                    .build();
            GlobalVar.getMyBot().execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            GlobalVar.log(Level.WARNING, "Can not send message", e);
        }
    }

    public static void editMessage(Long chatId, Integer messageId, String text){
        try {
            EditMessageText editMessageText = EditMessageText.builder()
                    .chatId(chatId)
                    .messageId(messageId)
                    .text(text)
                    .build();
            GlobalVar.getMyBot().execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            GlobalVar.log(Level.WARNING, "Can not edit message", e);
        }
    }

    public static void editMessage(Long chatId, Integer messageId, MessageKey key){
        try {
            EditMessageText editMessageText = EditMessageText.builder()
                    .chatId(chatId)
                    .messageId(messageId)
                    .text(i18nService.getMsg(key, chatId))
                    .build();
            GlobalVar.getMyBot().execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            GlobalVar.log(Level.WARNING, "Can not edit message", e);
        }
    }

    public static void editMessage(Long chatId, Integer messageId, String text, ReplyKeyboard inlineReplyMarkup){
        try {
            EditMessageText editMessageText = EditMessageText.builder()
                    .chatId(chatId)
                    .messageId(messageId)
                    .text(text)
                    .replyMarkup((InlineKeyboardMarkup) inlineReplyMarkup)
                    .build();
            GlobalVar.getMyBot().execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            GlobalVar.log(Level.WARNING, "Can not edit message", e);
        }
    }

    public static void editMessage(Long chatId, Integer messageId, MessageKey key, ReplyKeyboard inlineReplyMarkup){
        try {
            EditMessageText editMessageText = EditMessageText.builder()
                    .chatId(chatId)
                    .messageId(messageId)
                    .text(i18nService.getMsg(key, chatId))
                    .replyMarkup((InlineKeyboardMarkup) inlineReplyMarkup)
                    .build();
            GlobalVar.getMyBot().execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            GlobalVar.log(Level.WARNING, "Can not edit message", e);
        } catch (ClassCastException e){
            e.printStackTrace();
            GlobalVar.log(Level.WARNING, "can not Inline keyboard cast", e);
        }
    }

    public static void deleteMessage(Long chatId, Integer messageId){
        try {
            DeleteMessage deleteMessage = DeleteMessage.builder()
                    .chatId(chatId)
                    .messageId(messageId)
                    .build();
            GlobalVar.getMyBot().execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            GlobalVar.log(Level.WARNING, "Can not edit message", e);
        }
    }
}
