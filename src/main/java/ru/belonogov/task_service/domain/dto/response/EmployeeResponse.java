package ru.belonogov.task_service.domain.dto.response;

public class EmployeeResponse {

    private String firstName;
    private String lastName;
    private int rating;
    private String companyName;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getRating() {
        return rating;
    }

    public String getCompanyName() {
        return companyName;
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

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
