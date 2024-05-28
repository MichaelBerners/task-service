package domain.dto.response;

import domain.entity.TaskStatus;

public class TaskResponse {

    private String name;
    private String description;
    private int rating;
    private String taskStatus;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getRating() {
        return rating;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }
}
