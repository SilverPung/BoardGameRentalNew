package dev.apibaras.boardgamerental.exception;

public class UniqueValueException extends RuntimeException {
    public UniqueValueException(String message) {
        super(message);
    }
}
