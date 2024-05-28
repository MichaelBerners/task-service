package domain.entity;

import java.util.List;

public class Employee {

    private Long id;
    private String firstName;
    private String lastName;
    private int rating;
    private Company company;
    private List<Task> tasks;

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

    public List<Task> getTasks() {
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

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}