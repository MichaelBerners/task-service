package domain.repository;

import domain.dto.request.CompanyRequest;
import domain.dto.request.EmployeeRequest;
import domain.entity.Company;
import domain.entity.Employee;

import java.util.Optional;

public interface EmployeeDao {

    Employee save(EmployeeRequest employeeRequest);

    Optional<Employee> findById(Long id);

    Employee save(Long id, EmployeeRequest companyRequest);

    void delete (Long id);
}
