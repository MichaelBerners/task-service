package ru.belonogov.task_service.domain.dto.request;

import ru.belonogov.task_service.domain.entity.TaskStatus;

public class TaskUpdateRequest {
    private Long id;
    private String name;
    private String description;
    private int rating;
    private TaskStatus taskStatus;

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

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }
}
