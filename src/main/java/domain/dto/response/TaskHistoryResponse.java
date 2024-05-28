package domain.dto.response;

import domain.entity.Task;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TaskHistoryResponse {

    private List<Task> tasks;
    private String taskStatus;
    private LocalDate date;
    private LocalTime time;

    public List<Task> getTasks() {
        return tasks;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
