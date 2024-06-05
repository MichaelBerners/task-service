package ru.belonogov.task_service.domain.exception;

public class SaveException extends RuntimeException {
    public SaveException(String message) {
        super(message);
    }
}
