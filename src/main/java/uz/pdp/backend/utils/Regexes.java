package uz.pdp.backend.utils;

public interface Regexes {
    String PHONE_NUMBER_REGEX = "^[\\+][0-9]{3}[0-9]{3}[0-9]{4,6}$";
    String BIRTH_DATE = "\\d{2}.\\d{2}.\\d{4}";
}
