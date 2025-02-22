package de.telran.gartenshop.exception;

public class UserSaveException extends RuntimeException {
    public UserSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
