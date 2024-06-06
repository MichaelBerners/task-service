package ru.belonogov.task_service.domain.dto.response;

import java.util.List;
import java.util.Set;

public class CompanyResponse {

    private String name;

    private Set<String> employees;

    public String getName() {
        return name;
    }

    public Set<String> getEmployees() {
        return employees;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmployees(Set<String> employees) {
        this.employees = employees;
    }

}
