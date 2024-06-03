package ru.belonogov.task_service.domain.entity;

import java.util.List;
import java.util.Set;

public class Employee {

    private Long id;
    private String firstName;
    private String lastName;
    private int rating;
    private Company company;
    private Set<Task> tasks;

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getRating() {
        return rating;
    }

    public Company getCompany() {
        return company;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }
}
