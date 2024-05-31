package ru.belonogov.task_service.domain.dto.request;

public class TaskUpdateRequest {
    private Long id;
    private String name;
    private String description;
    private int rating;

    public Long getId() {
        return id;
    }

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
