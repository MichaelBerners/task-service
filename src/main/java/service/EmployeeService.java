package service;

import domain.dto.request.EmployeeRequest;
import domain.dto.response.EmployeeResponse;
import domain.entity.Employee;

import java.util.Optional;

public interface EmployeeService {

    EmployeeResponse save(EmployeeRequest employeeRequest);

    EmployeeResponse findById(Long id);

    EmployeeResponse save(Long id, EmployeeRequest companyRequest);

    void delete (Long id);
}
