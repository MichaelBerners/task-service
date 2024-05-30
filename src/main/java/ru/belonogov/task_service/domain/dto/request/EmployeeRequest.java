package ru.belonogov.task_service.domain.dto.request;

public class EmployeeRequest {

    private String firstName;
    private String lastName;
    private String companyName;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCompanyName() {
        return companyName;
    }
}
