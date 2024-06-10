package ru.belonogov.task_service.domain.exception;

public class DatabaseInterectionException extends RuntimeException {
    public DatabaseInterectionException(String message) {
        super(message);
    }
}
