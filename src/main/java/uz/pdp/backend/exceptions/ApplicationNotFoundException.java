package uz.pdp.backend.exceptions;

public class ApplicationNotFoundException extends Exception{
    public ApplicationNotFoundException() {
    }

    public ApplicationNotFoundException(String message) {
        super(message);
    }
}
