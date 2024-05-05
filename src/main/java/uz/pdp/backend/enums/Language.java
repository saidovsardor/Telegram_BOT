package uz.pdp.backend.enums;

import lombok.Getter;


@Getter
public enum Language {
    ENGLISH("en", "English \uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC65\uDB40\uDC6E\uDB40\uDC67\uDB40\uDC7F"),
    UZBEK("uz", "Uzbek \uD83C\uDDFA\uD83C\uDDFF"),
    RUSSIAN("ru", "Русский \uD83C\uDDF7\uD83C\uDDFA"),



    ;
    private String callbackData;
    private String buttonText;

    Language(String callbackData, String buttonText) {
        this.callbackData = callbackData;
        this.buttonText = buttonText;
    }
}
