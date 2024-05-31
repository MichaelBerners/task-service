package ru.belonogov.task_service.service.impl;

import ru.belonogov.task_service.domain.dto.mapper.EmployeeMapper;
import ru.belonogov.task_service.domain.dto.request.EmployeeRequest;
import ru.belonogov.task_service.domain.dto.request.EmployeeUpdateRequest;
import ru.belonogov.task_service.domain.dto.response.EmployeeResponse;
import ru.belonogov.task_service.domain.entity.Company;
import ru.belonogov.task_service.domain.entity.Employee;
import ru.belonogov.task_service.domain.exception.EmployeeNotFoundException;
import ru.belonogov.task_service.domain.repository.EmployeeDao;
import ru.belonogov.task_service.service.CompanyService;
import ru.belonogov.task_service.service.EmployeeService;

import java.util.Optional;

public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeDao employeeDao;
    private final CompanyService companyService;
    private final EmployeeMapper employeeMapper = EmployeeMapper.INSTANCE;

    public EmployeeServiceImpl(EmployeeDao employeeDao, CompanyService companyService) {
        this.employeeDao = employeeDao;
        this.companyService = companyService;
    }

    @Override
    public EmployeeResponse save(EmployeeRequest employeeRequest) {
        Company company = companyService.read(employeeRequest.getCompanyName());
        int defaultRating = 5;
        Employee save = employeeDao.save(employeeRequest.getFirstName(), employeeRequest.getLastName(), 5, company);

        return employeeMapper.employeeToEmployeeResponse(save);
    }

    @Override
    public EmployeeResponse findById(Long id) {
        Employee result = employeeDao.findById(id).orElseThrow(() -> new EmployeeNotFoundException());

        return employeeMapper.employeeToEmployeeResponse(result);
    }

    @Override
    public EmployeeResponse update(EmployeeUpdateRequest employeeUpdateRequest) {
        Employee employee = employeeDao.update(employeeUpdateRequest);

        return employeeMapper.employeeToEmployeeResponse(employee);
    }

    @Override
    public void delete(Long id) {
        employeeDao.delete(id);
    }
}
