package ru.belonogov.task_service.service;

import ru.belonogov.task_service.domain.dto.request.EmployeeRequest;
import ru.belonogov.task_service.domain.dto.request.EmployeeUpdateRequest;
import ru.belonogov.task_service.domain.dto.request.TaskEmployeeRequest;
import ru.belonogov.task_service.domain.dto.response.EmployeeResponse;
import ru.belonogov.task_service.domain.entity.Employee;

import java.util.List;

public interface EmployeeService {

    EmployeeResponse save(EmployeeRequest employeeRequest);

    EmployeeResponse findById(Long id);

    List<EmployeeResponse> findAllByTask(String taskName);

    EmployeeResponse update(EmployeeUpdateRequest employeeUpdateRequest);

    void addNewTask(TaskEmployeeRequest taskEmployeeRequest);

    boolean delete (Long id);
}
