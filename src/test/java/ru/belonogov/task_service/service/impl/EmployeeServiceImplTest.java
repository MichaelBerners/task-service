package ru.belonogov.task_service.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.belonogov.task_service.domain.dto.mapper.EmployeeMapper;
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
    @Captor
    private ArgumentCaptor<Employee> employeeArgumentCaptor;


    @Test
    void testSave_shouldReturnEmployeeDAO_whenCompanyExist() {
        EmployeeRequest employeeRequest = new EmployeeRequest();
        employeeRequest.setFirstName("Igor");
        employeeRequest.setLastName("Nikitin");
        employeeRequest.setCompanyName("Gazprom");
        Company company = new Company();
        company.setName(employeeRequest.getCompanyName());
        Employee employee = new Employee();
        EmployeeResponse employeeResponse = new EmployeeResponse();
        when(companyDao.findByName("Gazprom")).thenReturn(Optional.of(company));
        when(employeeDao.save(employeeArgumentCaptor.capture())).thenReturn(employee);
        when(employeeMapper.employeeToEmployeeResponse(employee)).thenReturn(employeeResponse);

        EmployeeResponse result = employeeService.save(employeeRequest);

        assertThat(result).isEqualTo(employeeResponse);
        assertThat(employeeArgumentCaptor.getValue())
                .matches(e -> e.getFirstName().equals(employeeRequest.getFirstName()))
                .matches(e -> e.getLastName().equals(employeeRequest.getLastName()))
                .matches(e -> e.getRating() == 5)
                .matches(e -> e.getTasks().isEmpty())
                .matches(e -> e.getCompany().getName().equals(employeeRequest.getCompanyName()));
        verify(companyDao).findByName("Gazprom");
        verify(employeeDao).save(any());
        verify(employeeMapper).employeeToEmployeeResponse(employee);

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
    void testFindById_shouldReturnEmployeeDTO_whenEmployeeExist() {
        Long id = 1L;
        Employee employee = new Employee();
        employee.setId(id);
        EmployeeResponse employeeResponse = new EmployeeResponse();
        when(employeeDao.findById(id)).thenReturn(Optional.of(employee));
        when(employeeMapper.employeeToEmployeeResponse(employee)).thenReturn(employeeResponse);

        EmployeeResponse result = employeeService.findById(id);

        assertThat(result).isEqualTo(employeeResponse);
        verify(employeeDao).findById(id);
        verify(employeeMapper).employeeToEmployeeResponse(employee);
    }

    @Test
    void testFindById_shouldReturnEmployeeNotFoundException_whenEmployeeIsNotExist() {
        Long id = 1L;
        when(employeeDao.findById(id)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.findById(id));
    }

    @Test//?
    void testFindAllByTask_shouldListEmployeeDAO_whenTaskExist() {
        String taskName = "Заливка фундамента";
        Employee employee1 = new Employee();
        Employee employee2 = new Employee();
        List<Employee> employees = List.of(employee1, employee2);
        EmployeeResponse employeeResponse = new EmployeeResponse();
        when(employeeDao.findAllByTask(taskName)).thenReturn(employees);
        when(employeeMapper.employeeToEmployeeResponse(employeeArgumentCaptor.capture())).thenReturn(employeeResponse);

        List<EmployeeResponse> result = employeeService.findAllByTask(taskName);

        assertThat(result)
                .isNotNull()
                .isNotEmpty();
        assertThat(employeeArgumentCaptor.getAllValues().contains(employee1)).isTrue();
        assertThat(employeeArgumentCaptor.getAllValues().contains(employee2)).isTrue();
        verify(employeeDao).findAllByTask(taskName);
        verify(employeeMapper, times(2)).employeeToEmployeeResponse(any());
    }

    @Test
    void testFindAllByTask_shouldEmptyList_whenTaskIsNotExist() {
        String taskName = "Заливка фундамента";
        when(employeeDao.findAllByTask(taskName)).thenReturn(Collections.emptyList());

        List<EmployeeResponse> result = employeeService.findAllByTask(taskName);

        assertThat(result).isEmpty();
        verify(employeeDao).findAllByTask(taskName);
    }

    @Test
    void testUpdate_shouldEmployeeDTO_whenEmployeeExist() {
        EmployeeUpdateRequest employeeUpdateRequest = new EmployeeUpdateRequest();
        employeeUpdateRequest.setId(1L);
        employeeUpdateRequest.setRating(7);
        Employee employee = new Employee();
        EmployeeResponse employeeResponse = new EmployeeResponse();
        when(employeeDao.findById(employeeUpdateRequest.getId())).thenReturn(Optional.of(employee));
        when(employeeDao.update(employeeArgumentCaptor.capture())).thenReturn(employee);
        when(employeeMapper.employeeToEmployeeResponse(employee)).thenReturn(employeeResponse);

        EmployeeResponse result = employeeService.update(employeeUpdateRequest);

        assertThat(result).isEqualTo(employeeResponse);
        assertThat(employeeArgumentCaptor.getValue())
                .matches(e -> e.getId() == employeeUpdateRequest.getId())
                .matches(e -> e.getRating() == employeeUpdateRequest.getRating());
        verify(employeeDao).findById(employeeUpdateRequest.getId());
        verify(employeeDao).update(any());
        verify(employeeMapper).employeeToEmployeeResponse(employee);
    }

    @Test
    void testAddNewTask_whenEmployeeAndTaskExist() {
        TaskEmployeeRequest taskEmployeeRequest = new TaskEmployeeRequest();
        taskEmployeeRequest.setTaskId(1L);
        taskEmployeeRequest.setEmployeeId(2L);
        Task task = new Task();
        Employee employee = new Employee();
        when(taskDao.findById(1L)).thenReturn(Optional.of(task));
        when(employeeDao.findById(2L)).thenReturn(Optional.of(employee));

        employeeService.addNewTask(taskEmployeeRequest);

        verify(taskDao).findById(1L);
        verify(employeeDao).findById(2L);
        verify(employeeDao).addNewTask(1L, 2L);
    }

    @Test
    void testAddNewTask_whenEmployeeOrTaskIsNotExist() {
        TaskEmployeeRequest taskEmployeeRequest = new TaskEmployeeRequest();
        taskEmployeeRequest.setTaskId(1L);
        taskEmployeeRequest.setEmployeeId(2L);
        when(taskDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AddNewTaskException.class, () -> employeeService.addNewTask(taskEmployeeRequest));
    }

    @Test
    void testDelete_shouldReturnTrue_whenEmployeeExist() {
        Long id = 1L;
        Employee employee = new Employee();
        when(employeeDao.findById(id)).thenReturn(Optional.of(employee));

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