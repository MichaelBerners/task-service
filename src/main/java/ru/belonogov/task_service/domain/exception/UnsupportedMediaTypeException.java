package ru.belonogov.task_service.domain.exception;

public class UnsupportedMediaTypeException extends RuntimeException{
    public UnsupportedMediaTypeException(Throwable cause) {
        super(cause);
    }

    public UnsupportedMediaTypeException(String message) {
        super(message);
    }
}
