package ru.belonogov.task_service.domain.repository;

import ru.belonogov.task_service.domain.dto.request.EmployeeRequest;
import ru.belonogov.task_service.domain.entity.Company;
import ru.belonogov.task_service.domain.entity.Employee;

import java.util.Optional;

public interface EmployeeDao {

    Employee save(String firstName, String lastName, int rating, Company company);

    Optional<Employee> findById(Long id);

    Employee update(Long id, int rating);

    void delete (Long id);
}
