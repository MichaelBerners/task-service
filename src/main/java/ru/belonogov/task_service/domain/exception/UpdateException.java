package ru.belonogov.task_service.domain.exception;

public class UpdateException extends RuntimeException{

    public UpdateException(String message) {
        super(message);
    }
}
