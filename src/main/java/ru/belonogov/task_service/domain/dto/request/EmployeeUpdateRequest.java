package ru.belonogov.task_service.domain.dto.request;

public class EmployeeUpdateRequest {

    private Long id;
    private int rating;

    public Long getId() {
        return id;
    }

    public int getRating() {
        return rating;
    }
}
