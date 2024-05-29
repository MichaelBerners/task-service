package ru.belonogov.task_service.domain.dto.response;

import ru.belonogov.task_service.domain.entity.Task;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TaskHistoryResponse {

    private String task;
    private String taskStatus;
    private LocalDate date;
    private LocalTime time;

    public String  getTask() {
        return task;
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

    public void setTask(String task) {
        this.task = task;
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
