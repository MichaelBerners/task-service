package domain.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TaskHistory {

    private Long id;
    private List<Task> tasks;
    private TaskStatus taskStatus;
    private LocalDate date;
    private LocalTime time;

    public Long getId() {
        return id;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
