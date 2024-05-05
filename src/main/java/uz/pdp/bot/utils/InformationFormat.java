package uz.pdp.bot.utils;

import java.util.StringJoiner;

public interface InformationFormat {
    public static String getFullName(String firstName, String lastName){
        StringJoiner res = new StringJoiner(" ");
        if(firstName!=null && !firstName.isBlank()) res.add(firstName);
        if(lastName!=null && !lastName.isBlank()) res.add(lastName);
        return res.toString();
    }
}
