package ru.belonogov.task_service.service;

import ru.belonogov.task_service.domain.dto.request.EmployeeRequest;
import ru.belonogov.task_service.domain.dto.response.EmployeeResponse;

public interface EmployeeService {

    EmployeeResponse save(EmployeeRequest employeeRequest);

    EmployeeResponse findById(Long id);

    EmployeeResponse save(Long id, EmployeeRequest companyRequest);

    void delete (Long id);
}
