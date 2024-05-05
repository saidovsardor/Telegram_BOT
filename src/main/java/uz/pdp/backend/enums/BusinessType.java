package uz.pdp.backend.enums;

import lombok.Getter;

@Getter
public enum BusinessType {
    SPORT("Sport"),
    TOY("Toy"),
    FOOD("Food"),
    ELECTRONIC("Electronic"),
    MEDICINE("Medicine")

    ;

    private final String buttonName;

    BusinessType(String buttonName) {
        this.buttonName = buttonName;
    }

}
