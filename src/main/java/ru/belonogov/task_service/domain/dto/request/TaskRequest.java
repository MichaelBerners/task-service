package ru.belonogov.task_service.domain.dto.request;

public class TaskRequest {

    private String name;
    private String description;
    private int rating;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getRating() {
        return rating;
    }
}
