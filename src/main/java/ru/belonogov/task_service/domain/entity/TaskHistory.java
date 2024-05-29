package ru.belonogov.task_service.domain.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TaskHistory {

    private Long id;
    private Task task;
    private TaskStatus taskStatus;
    private LocalDate date;
    private LocalTime time;

    public Long getId() {
        return id;
    }

    public Task getTask() {
        return task;
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

    public void setTask(Task task) {
        this.task = task;
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
