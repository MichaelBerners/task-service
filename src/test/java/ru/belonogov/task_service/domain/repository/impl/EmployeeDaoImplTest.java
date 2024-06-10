package ru.belonogov.task_service.domain.repository.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.belonogov.task_service.PostgresTestContainer;
import ru.belonogov.task_service.domain.entity.Company;
import ru.belonogov.task_service.domain.entity.Employee;
import ru.belonogov.task_service.domain.entity.Task;
import ru.belonogov.task_service.domain.exception.DatabaseInterectionException;
import ru.belonogov.task_service.domain.exception.SaveException;
import ru.belonogov.task_service.domain.exception.UpdateException;
import ru.belonogov.task_service.domain.repository.EmployeeDao;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class EmployeeDaoImplTest {

    private static EmployeeDao employeeDao;

    private static PostgresTestContainer postgresTestContainer;

    @BeforeAll
    static void init(){
        postgresTestContainer = PostgresTestContainer.getInstance();
        employeeDao = new EmployeeDaoImpl();
    }

    @Test
    void testSave_shouldSaveAndReturnEmployee() {
        Employee employee = new Employee();
        employee.setFirstName("Alexei");
        employee.setLastName("Bubnov");
        employee.setRating(5);
        Company company = new Company();
        company.setId(1L);
        company.setName("Gazprom");
        company.setEmployees(Collections.emptySet());
        employee.setCompany(company);

        Employee result = employeeDao.save(employee);

        assertThat(result)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("firstName", "Alexei");
    }

    @Test
    void testSave_shouldSaveAndReturnSaveException() {
        Employee employee = new Employee();
        employee.setFirstName("Alexei");
        employee.setLastName("Bubnov");
        employee.setRating(5);
        Company company = new Company();
        company.setId(20L);
        company.setName("Gazprom");
        company.setEmployees(Collections.emptySet());
        employee.setCompany(company);

        assertThrows(SaveException.class, () -> employeeDao.save(employee));
    }


    @Test
    void testFindById_shouldReturnOptionalOfEmployee_whenEmployeeExist() {
        assertTrue(employeeDao.findById(1L).isPresent());
    }

    @Test
    void testFindById_shouldReturnOptionalOfEmpty_whenEmployeeIsNotExist() {
        assertFalse(employeeDao.findById(100L).isPresent());
    }

    @Test
    void testFindAllByTask_shouldReturnListEmployee_whenTaskExist() {
        assertThat(employeeDao.findAllByTask("Заливка фундамента")).isNotEmpty();
    }

    @Test
    void testFindAllByTask_shouldReturnEmptyList_whenTaskIsNotExist() {
        assertThat(employeeDao.findAllByTask("Благоустройство территории")).isEmpty();
    }

    @Test
    void testUpdate_shouldReturnUpdateEmployee_whenEmployeeExist() {
        Employee employee = new Employee();
        employee.setId(4L);
        employee.setRating(5);

        Employee result = employeeDao.update(employee);

        assertThat(result)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("rating", 5);
    }

    @Test
    void testUpdate_shouldReturnUpdateException_whenEmployeeIsNotExist() {
        Employee employee = new Employee();
        employee.setId(200L);
        employee.setRating(5);

        assertThrows(UpdateException.class, () -> employeeDao.update(employee));
    }

    @Test
    void testAddNewTask_shouldReturnTrue_whenEmployeeAndTaskExist() {
        assertThat(employeeDao.addNewTask(4L, 3L)).isTrue();
    }

    @Test
    void testAddNewTask_shouldReturnFalse_whenEmployeeOrTaskIsNotExist() {
        assertThat(employeeDao.addNewTask(30L, 30L)).isFalse();
    }

    @Test
    void testDelete_shouldReturnTrue_whenEmployeeDelete() {
        assertThat(employeeDao.delete(2L)).isTrue();
    }

    @Test
    void testDelete_shouldReturnFalse_whenEmployeeIsExist() {
        assertThat(employeeDao.delete(100L)).isFalse();
    }
    @Test
    void testDelete_shouldReturnDatabaseInterectionException_whenEmployeeHasARelationship() {
        assertThrows(DatabaseInterectionException.class, () -> employeeDao.delete(1L));
    }
}