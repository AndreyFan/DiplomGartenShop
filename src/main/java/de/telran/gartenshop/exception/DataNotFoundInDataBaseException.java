package de.telran.gartenshop.exception;

public class DataNotFoundInDataBaseException extends RuntimeException {
    public DataNotFoundInDataBaseException(String message) {
        super(message);
    }
}
