package ru.belonogov.task_service.domain.exception;

public class AddNewTaskException extends RuntimeException{

    public AddNewTaskException(String message) {
        super(message);
    }
}
