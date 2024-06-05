package ru.belonogov.task_service.domain.entity;

import java.util.List;
import java.util.Set;

public class Company {

    private Long id;
    private String name;
    private Set<Employee> employees;

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

}
