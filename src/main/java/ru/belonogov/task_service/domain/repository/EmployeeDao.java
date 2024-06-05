package ru.belonogov.task_service.domain.repository;

import ru.belonogov.task_service.domain.dto.request.EmployeeRequest;
import ru.belonogov.task_service.domain.dto.request.EmployeeUpdateRequest;
import ru.belonogov.task_service.domain.entity.Company;
import ru.belonogov.task_service.domain.entity.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeDao {

    Employee save(Employee employee);

    Optional<Employee> findById(Long id);

    List<Employee> findAllByTask(String taskName);

    Employee update(EmployeeUpdateRequest employeeUpdateRequest);

    boolean addNewTask(Long employeeId, Long taskId);

    void delete (Long id);
}
