package uz.pdp.backend.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static uz.pdp.backend.utils.Regexes.*;

public interface CheckUserInformation {
    static boolean checkPhoneNumber(String phoneNumber){
        Pattern pattern = Pattern.compile(PHONE_NUMBER_REGEX);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
}
