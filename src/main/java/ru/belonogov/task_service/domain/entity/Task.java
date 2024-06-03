package ru.belonogov.task_service.domain.entity;

import java.util.List;
import java.util.Set;

public class Task {

    private Long id;
    private String name;
    private String description;
    private int rating;
    private TaskStatus taskStatus;
    private Set<Employee> employees;

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

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }
}
