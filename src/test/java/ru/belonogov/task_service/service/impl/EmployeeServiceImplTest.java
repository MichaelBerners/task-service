package ru.belonogov.task_service.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.belonogov.task_service.domain.dto.mapper.EmployeeMapper;
import ru.belonogov.task_service.domain.dto.request.CompanySaveRequest;
import ru.belonogov.task_service.domain.dto.request.EmployeeRequest;
import ru.belonogov.task_service.domain.dto.request.EmployeeUpdateRequest;
import ru.belonogov.task_service.domain.dto.request.TaskEmployeeRequest;
import ru.belonogov.task_service.domain.dto.response.EmployeeResponse;
import ru.belonogov.task_service.domain.entity.Company;
import ru.belonogov.task_service.domain.entity.Employee;
import ru.belonogov.task_service.domain.entity.Task;
import ru.belonogov.task_service.domain.exception.AddNewTaskException;
import ru.belonogov.task_service.domain.exception.CompanyNotFoundException;
import ru.belonogov.task_service.domain.exception.EmployeeNotFoundException;
import ru.belonogov.task_service.domain.exception.UpdateException;
import ru.belonogov.task_service.domain.repository.CompanyDao;
import ru.belonogov.task_service.domain.repository.EmployeeDao;
import ru.belonogov.task_service.domain.repository.TaskDao;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeDao employeeDao;
    @Mock
    private CompanyDao companyDao;
    @Mock
    private TaskDao taskDao;
    @Mock
    private EmployeeMapper employeeMapper;
    @InjectMocks
    private EmployeeServiceImpl employeeService;


    @Test
    void testSave_shouldReturnEmployeeDAO_whenCompanyExist() {
        EmployeeRequest employeeRequest = new EmployeeRequest();
        employeeRequest.setFirstName("Igor");
        employeeRequest.setLastName("Nikitin");
        employeeRequest.setCompanyName("Gazprom");
        Company company = new Company();
        company.setName(employeeRequest.getCompanyName());
        when(companyDao.findByName(employeeRequest.getCompanyName())).thenReturn(Optional.ofNullable(company));
        when(employeeDao.save(argThat(arg -> {
            assertThat(arg.getCompany().getName().equals("Gazprom")).isTrue();
            assertThat(arg.getRating() == 5).isTrue();
            return true;
        }))).thenAnswer(e -> e.getArgument(0));
        when(employeeMapper.employeeToEmployeeResponse(argThat(arg -> {
            assertThat(arg.getCompany().getName().equals("Gazprom")).isTrue();
            assertThat(arg.getRating() == 5).isTrue();
            return true;
        }))).thenReturn(mock(EmployeeResponse.class));

        employeeService.save(employeeRequest);

        verify(companyDao).findById(any());
        verify(employeeDao).save(any());
        verify(employeeMapper).employeeToEmployeeResponse(any());

    }

    @Test
    void testSave_shouldReturnCompanyNotFoundException_whenCompanyIsNotExist() {
        EmployeeRequest employeeRequest = new EmployeeRequest();
        employeeRequest.setFirstName("Igor");
        employeeRequest.setLastName("Nikitin");
        employeeRequest.setCompanyName("Gazprom");
        when(companyDao.findByName(employeeRequest.getCompanyName())).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class, () -> employeeService.save(employeeRequest));
    }

    @Test
    void testFindById_shouldReturnDTO_whenEmployeeExist() {
        Long id = 1L;
        Employee employee = new Employee();
        employee.setId(id);
        when(employeeDao.findById(id)).thenReturn(Optional.ofNullable(employee));
        when(employeeMapper.employeeToEmployeeResponse(employee));

        employeeService.findById(id);

        verify(employeeDao.findById(id));
        verify(employeeMapper.employeeToEmployeeResponse(employee));
    }

    @Test
    void testFindById_shouldReturnEmployeeNotFoundException_whenEmployeeIsNotExist() {
        Long id = 1L;;
        when(employeeDao.findById(id)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class ,() -> employeeService.findById(id));

    }

    @Test
    void testFindAllByTask_shouldListEmployeeDAO_whenTaskExist() {
        String taskName = "Заливка фундамента";
        when(employeeDao.findAllByTask(taskName)).thenReturn(mock(List.class));
        when(employeeMapper.employeeToEmployeeResponse(any())).thenReturn(mock(EmployeeResponse.class));

        employeeService.findAllByTask(taskName);

        verify(employeeDao).findAllByTask(taskName);
        verify(employeeMapper.employeeToEmployeeResponse(any()));
    }

    @Test
    void testFindAllByTask_shouldEmptyList_whenTaskIsNotExist() {
        String taskName = "Заливка фундамента";
        when(employeeDao.findAllByTask(taskName)).thenReturn(Collections.emptyList());

        assertThat(employeeService.findAllByTask(taskName)).isEmpty();

        verify(employeeDao).findAllByTask(taskName);
    }

    @Test
    void testUpdate_shouldEmployeeDTO_whenEmployeeExist() {
        EmployeeUpdateRequest employeeUpdateRequest = new EmployeeUpdateRequest();
        employeeUpdateRequest.setId(1L);
        employeeUpdateRequest.setRating(7);
        Employee employee = new Employee();
        employee.setId(employeeUpdateRequest.getId());
        employee.setRating(employeeUpdateRequest.getRating());
        when(employeeDao.findById(employeeUpdateRequest.getId())).thenReturn(Optional.ofNullable(employee));
        when(employeeDao.update(employee)).thenReturn(employee);
        when(employeeMapper.employeeToEmployeeResponse(employee));

        employeeService.update(employeeUpdateRequest);

        verify(employeeDao).findAllByTask(any());
        verify(employeeDao).update(any());
        verify(employeeMapper).employeeToEmployeeResponse(any());
    }

    @Test
    void testAddNewTask_whenEmployeeAndTaskExist() {
        TaskEmployeeRequest taskEmployeeRequest = new TaskEmployeeRequest();
        taskEmployeeRequest.setTaskId(1L);
        taskEmployeeRequest.setEmployeeId(2L);
        when(taskDao.findById(1L)).thenReturn(mock(Optional.class));
        when(employeeDao.findById(2L)).thenReturn(mock(Optional.class));

        employeeService.addNewTask(taskEmployeeRequest);

        verify(taskDao.findById(1L));
        verify(employeeDao.findById(2L));
        verify(employeeDao.addNewTask(1L, 2L));
    }

    @Test
    void testAddNewTask_whenEmployeeOrTaskIsNotExist() {
        TaskEmployeeRequest taskEmployeeRequest = new TaskEmployeeRequest();
        taskEmployeeRequest.setTaskId(1L);
        taskEmployeeRequest.setEmployeeId(2L);
        when(taskDao.findById(1L)).thenReturn(Optional.empty());
        when(employeeDao.findById(2L)).thenReturn(mock(Optional.class));

        assertThrows(AddNewTaskException.class, () -> employeeService.addNewTask(taskEmployeeRequest));
    }

    @Test
    void testDelete_shouldReturnTrue_whenEmployeeExist() {
        Long id = 1L;
        when(employeeDao.findById(id)).thenReturn(mock(Optional.class));

        employeeService.delete(id);

        verify(employeeDao).findById(id);
        verify(employeeDao).delete(id);
    }

    @Test
    void testDelete_shouldReturnUpdateException_whenEmployeeIsNotExist() {
        Long id = 1L;
        when(employeeDao.findById(id)).thenReturn(Optional.empty());

        assertThrows(UpdateException.class, () -> employeeService.delete(id));

    }
}