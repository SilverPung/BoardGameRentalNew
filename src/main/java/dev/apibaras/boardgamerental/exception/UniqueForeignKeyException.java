package dev.apibaras.boardgamerental.exception;

public class UniqueForeignKeyException extends RuntimeException {
    public UniqueForeignKeyException(String message) {
        super(message);
    }
}
