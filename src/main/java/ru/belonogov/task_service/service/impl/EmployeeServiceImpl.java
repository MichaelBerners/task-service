package ru.belonogov.task_service.service.impl;

import ru.belonogov.task_service.domain.dto.mapper.EmployeeMapper;
import ru.belonogov.task_service.domain.dto.request.EmployeeRequest;
import ru.belonogov.task_service.domain.dto.request.EmployeeUpdateRequest;
import ru.belonogov.task_service.domain.dto.request.TaskEmployeeRequest;
import ru.belonogov.task_service.domain.dto.response.EmployeeResponse;
import ru.belonogov.task_service.domain.entity.Company;
import ru.belonogov.task_service.domain.entity.Employee;
import ru.belonogov.task_service.domain.exception.AddNewTaskException;
import ru.belonogov.task_service.domain.exception.CompanyNotFoundException;
import ru.belonogov.task_service.domain.exception.EmployeeNotFoundException;
import ru.belonogov.task_service.domain.exception.UpdateException;
import ru.belonogov.task_service.domain.repository.CompanyDao;
import ru.belonogov.task_service.domain.repository.EmployeeDao;
import ru.belonogov.task_service.domain.repository.TaskDao;
import ru.belonogov.task_service.service.EmployeeService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeDao employeeDao;
    private final CompanyDao companyDao;
    private final TaskDao taskDao;
    private final EmployeeMapper employeeMapper;

    public EmployeeServiceImpl(EmployeeDao employeeDao, CompanyDao companyDao, TaskDao taskDao, EmployeeMapper employeeMapper) {
        this.employeeDao = employeeDao;
        this.companyDao = companyDao;
        this.taskDao = taskDao;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public EmployeeResponse save(EmployeeRequest employeeRequest) {
        Company company = companyDao.findByName(employeeRequest.getCompanyName()).orElseThrow(() -> new CompanyNotFoundException("Компания не найдена"));
        int defaultRating = 5;
        Employee employee = new Employee();
        employee.setFirstName(employeeRequest.getFirstName());
        employee.setLastName(employee.getLastName());
        employee.setRating(defaultRating);
        employee.setCompany(company);
        employee.setTasks(Collections.emptySet());
        Employee save = employeeDao.save(employee);

        return employeeMapper.employeeToEmployeeResponse(save);
    }

    @Override
    public EmployeeResponse findById(Long id) {
        Employee result = employeeDao.findById(id).orElseThrow(() -> new EmployeeNotFoundException("Работник не найден"));

        return employeeMapper.employeeToEmployeeResponse(result);
    }

    @Override
    public List<EmployeeResponse> findAllByTask(String taskName) {
        List<Employee> allByTask = employeeDao.findAllByTask(taskName);
        if(allByTask.isEmpty()) {
            return Collections.emptyList();
        }
        List<EmployeeResponse> result = allByTask.stream()
                .map($ -> employeeMapper.employeeToEmployeeResponse($))
                .collect(Collectors.toList());

        return result;
    }

    @Override
    public EmployeeResponse update(EmployeeUpdateRequest employeeUpdateRequest) {
        Employee employee = new Employee();
        employee.setId(employee.getId());
        employee.setRating(employee.getRating());
        if(employeeDao.findById(employee.getId()).isEmpty()) {
            throw new UpdateException("Работник которого требуется удалить не найден");
        }
        Employee employeeUpdate = employeeDao.update(employee);

        return employeeMapper.employeeToEmployeeResponse(employeeUpdate);
    }

    @Override
    public void addNewTask(TaskEmployeeRequest taskEmployeeRequest) {
        Long taskId = taskEmployeeRequest.getTaskId();
        Long employeeId = taskEmployeeRequest.getEmployeeId();
        if(taskDao.findById(taskId).isEmpty() || employeeDao.findById(employeeId).isEmpty()) {
            throw new AddNewTaskException("Ошибка добавления нового задания работнику");
        }
        employeeDao.addNewTask(taskId, employeeId);
    }

    @Override
    public boolean delete(Long id) {
        if(employeeDao.findById(id).isEmpty()) {
            throw new UpdateException("Работник которого требуется удалить не найден");
        }
        return (employeeDao.delete(id));
    }
}
