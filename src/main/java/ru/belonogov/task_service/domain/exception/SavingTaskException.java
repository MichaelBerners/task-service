package ru.belonogov.task_service.domain.exception;

public class SavingTaskException extends RuntimeException{

    public SavingTaskException(String message) {
        super(message);
    }
}
