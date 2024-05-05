package uz.pdp.backend.exceptions;

public class WrongRoleException extends Exception{
    public WrongRoleException() {
    }

    public WrongRoleException(String message) {
        super(message);
    }
}
