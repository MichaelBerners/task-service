package ru.belonogov.task_service.domain.repository;

import ru.belonogov.task_service.domain.dto.request.EmployeeRequest;
import ru.belonogov.task_service.domain.entity.Employee;

import java.util.Optional;

public interface EmployeeDao {

    Employee save(String firstName, String lastName, int rating, Long companyId);

    Optional<Employee> findById(Long id);

    Employee save(Long id, EmployeeRequest companyRequest);

    void delete (Long id);
}
