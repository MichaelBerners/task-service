package ru.belonogov.task_service.domain.exception;

public class AddNewEmployeeException extends RuntimeException{

    public AddNewEmployeeException(String message) {
        super(message);
    }
}
