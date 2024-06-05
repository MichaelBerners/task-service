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

    public void setName(String name) {
        this.name = name;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
