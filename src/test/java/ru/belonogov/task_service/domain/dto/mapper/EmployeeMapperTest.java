package ru.belonogov.task_service.domain.dto.mapper;

import org.junit.jupiter.api.Test;
import ru.belonogov.task_service.domain.dto.response.EmployeeResponse;
import ru.belonogov.task_service.domain.entity.Company;
import ru.belonogov.task_service.domain.entity.Employee;
import ru.belonogov.task_service.domain.entity.Task;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class EmployeeMapperTest {

    private EmployeeMapper employeeMapper = EmployeeMapper.INSTANCE;

    @Test
    void employeeToEmployeeResponse_shouldEmployeeDTO_whenTaskExist() {
        Task task1 = new Task();
        task1.setName("Задание1");
        Task task2 = new Task();
        task2.setName("Задание2");
        Set<Task> tasks = Set.of(task1, task2);
        Company company = new Company();
        company.setName("Имя компании");
        Employee employee = new Employee();
        employee.setFirstName("Имя сотрудника");
        employee.setLastName("Фамилия сотрудника");
        employee.setRating(5);
        employee.setCompany(company);
        employee.setTasks(tasks);

        EmployeeResponse result = employeeMapper.employeeToEmployeeResponse(employee);

        assertThat(result)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .matches(e -> e.getFirstName().equals(employee.getFirstName()))
                .matches(e -> e.getLastName().equals(employee.getLastName()))
                .matches(e -> e.getRating() == employee.getRating())
                .matches(e -> e.getCompanyName().equals(employee.getCompany().getName()))
                .matches(e -> e.getTasks().size() == 2)
                .matches(e -> e.getTasks().contains("Задание1"))
                .matches(e -> e.getTasks().contains("Задание2"));
    }

    @Test
    void employeeToEmployeeResponse_shouldEmployeeDTO_whenTaskIsNotExist() {
        Company company = new Company();
        company.setName("Имя компании");
        Employee employee = new Employee();
        employee.setFirstName("Имя сотрудника");
        employee.setLastName("Фамилия сотрудника");
        employee.setRating(5);
        employee.setCompany(company);
        employee.setTasks(Collections.emptySet());

        EmployeeResponse result = employeeMapper.employeeToEmployeeResponse(employee);

        assertThat(result)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .matches(e -> e.getFirstName().equals(employee.getFirstName()))
                .matches(e -> e.getLastName().equals(employee.getLastName()))
                .matches(e -> e.getRating() == employee.getRating())
                .matches(e -> e.getCompanyName().equals(employee.getCompany().getName()))
                .matches(e -> e.getTasks().isEmpty());
    }
}