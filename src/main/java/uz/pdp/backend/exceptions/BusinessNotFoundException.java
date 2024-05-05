package uz.pdp.backend.exceptions;

public class BusinessNotFoundException extends Exception{
    public BusinessNotFoundException(String message) {
        super(message);
    }

    public BusinessNotFoundException() {
    }
}
