package ru.belonogov.task_service.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.belonogov.task_service.domain.dto.mapper.TaskMapper;
import ru.belonogov.task_service.domain.dto.request.TaskEmployeeRequest;
import ru.belonogov.task_service.domain.dto.request.TaskRequest;
import ru.belonogov.task_service.domain.dto.request.TaskUpdateRequest;
import ru.belonogov.task_service.domain.dto.response.TaskResponse;
import ru.belonogov.task_service.domain.entity.Employee;
import ru.belonogov.task_service.domain.entity.Task;
import ru.belonogov.task_service.domain.entity.TaskStatus;
import ru.belonogov.task_service.domain.exception.AddNewTaskException;
import ru.belonogov.task_service.domain.exception.TaskNotFoundException;
import ru.belonogov.task_service.domain.exception.UpdateException;
import ru.belonogov.task_service.domain.repository.EmployeeDao;
import ru.belonogov.task_service.domain.repository.TaskDao;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskMapper taskMapper;
    @Mock
    private TaskDao taskDao;
    @Mock
    private EmployeeDao employeeDao;
    @InjectMocks
    private TaskServiceImpl taskService;
    @Captor
    private ArgumentCaptor<Task> taskArgumentCaptor;

    @Test
    void testSave_shouldTaskDTO_whenTaskSaved() {
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setName("Новое задание");
        taskRequest.setDescription("Описание нового задания");
        taskRequest.setRating(7);
        Task task = new Task();
        TaskResponse taskResponse = new TaskResponse();
        when(taskDao.save(argThat(arg -> {
            assertThat(arg.getName().equals("Новое задание")).isTrue();
            assertThat(arg.getDescription().equals("Описание нового задания")).isTrue();
            assertThat(arg.getRating() == 7).isTrue();
            assertThat(arg.getTaskStatus() == TaskStatus.SEARCH_FOR_EMPLOYEES).isTrue();
            return true;
        }))).thenReturn(task);
        when(taskMapper.taskToTaskResponse(task)).thenReturn(taskResponse);

        TaskResponse result = taskService.save(taskRequest);

        assertThat(result).isEqualTo(taskResponse);
        verify(taskDao).save(any());
        verify(taskMapper).taskToTaskResponse(task);
    }

    @Test
    void testFindById_shouldReturnTaskDTO_whenTaskExist() {
        Long id = 1L;
        Task task = new Task();
        TaskResponse taskResponse = new TaskResponse();
        when(taskDao.findById(id)).thenReturn(Optional.of(task));
        when(taskMapper.taskToTaskResponse(task)).thenReturn(taskResponse);

        TaskResponse result = taskService.findById(id);

        assertThat(result).isEqualTo(taskResponse);
        verify(taskDao).findById(id);
        verify(taskMapper).taskToTaskResponse(task);
    }

    @Test
    void testFindById_shouldReturnTaskNotFoundException_whenTaskIsNotExist() {
        Long id = 1L;
        when(taskDao.findById(id)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.findById(id));

        verify(taskDao).findById(id);
    }

    @Test
    void testFindAllByEmployee_shouldReturnListTaskDAO_whenEmployeeExist() {
        Long id = 1L;
        Task task1 = new Task();
        Task task2 = new Task();
        List<Task> tasks = List.of(task1, task2);
        TaskResponse taskResponse = new TaskResponse();
        when(taskDao.findAllByEmployee(id)).thenReturn(tasks);
        when(taskMapper.taskToTaskResponse(taskArgumentCaptor.capture())).thenReturn(taskResponse);

        List<TaskResponse> result = taskService.findAllByEmployee(id);

        assertThat(result)
                .isNotNull()
                .isNotEmpty();
        assertThat(taskArgumentCaptor.getAllValues().contains(task1)).isTrue();
        assertThat(taskArgumentCaptor.getAllValues().contains(task2)).isTrue();
        verify(taskDao).findAllByEmployee(id);
        verify(taskMapper, times(2)).taskToTaskResponse(any());
    }

    @Test
    void testFindAllByEmployee_shouldReturnEmptyList_whenEmployeeIsNotExist() {
        Long id = 1L;
        when(taskDao.findAllByEmployee(id)).thenReturn(Collections.emptyList());

        List<TaskResponse> result = taskService.findAllByEmployee(id);

        assertThat(result).isEmpty();
        verify(taskDao).findAllByEmployee(id);
    }

    @Test
    void testUpdate_shouldReturnTaskDTO_whenTaskExist() {
        TaskUpdateRequest taskUpdateRequest = new TaskUpdateRequest();
        taskUpdateRequest.setId(1L);
        taskUpdateRequest.setName("Старое название");
        taskUpdateRequest.setDescription("Новое описание");
        taskUpdateRequest.setRating(5);
        taskUpdateRequest.setTaskStatus(TaskStatus.IN_PROGRESS);
        Task task = new Task();
        TaskResponse taskResponse = new TaskResponse();
        when(taskDao.findById(1L)).thenReturn(Optional.of(task));
        when(taskDao.update(taskArgumentCaptor.capture())).thenReturn(task);
        when(taskMapper.taskToTaskResponse(task)).thenReturn(taskResponse);

        TaskResponse result = taskService.update(taskUpdateRequest);

        assertThat(result).isEqualTo(taskResponse);
        assertThat(taskArgumentCaptor.getValue())
                .matches(e -> e.getId() == taskUpdateRequest.getId())
                .matches(e -> e. getName().equals(taskUpdateRequest.getName()))
                .matches(e -> e.getDescription().equals(taskUpdateRequest.getDescription()))
                .matches(e -> e.getRating() == taskUpdateRequest.getRating())
                .matches(e -> e.getTaskStatus() == taskUpdateRequest.getTaskStatus());
        verify(taskDao).findById(1L);
        verify(taskDao).update(any());
        verify(taskMapper).taskToTaskResponse(task);
    }

    @Test
    void taskUpdate_shouldReturnUpdateException_whenTaskExist() {
        TaskUpdateRequest taskUpdateRequest = new TaskUpdateRequest();
        taskUpdateRequest.setId(1L);
        taskUpdateRequest.setName("Старое название");
        taskUpdateRequest.setDescription("Новое описание");
        taskUpdateRequest.setRating(5);
        taskUpdateRequest.setTaskStatus(TaskStatus.IN_PROGRESS);
        when(taskDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UpdateException.class, () -> taskService.update(taskUpdateRequest));
    }

    @Test
    void testAddNewEmployee_whenEmployeeAndTaskExist() {
        TaskEmployeeRequest taskEmployeeRequest = new TaskEmployeeRequest();
        taskEmployeeRequest.setTaskId(1L);
        taskEmployeeRequest.setEmployeeId(2L);
        Task task = new Task();
        Employee employee = new Employee();
        when(taskDao.findById(1L)).thenReturn(Optional.of(task));
        when(employeeDao.findById(2L)).thenReturn(Optional.of(employee));

        taskService.addNewEmployeeToTask(taskEmployeeRequest);

        verify(taskDao).findById(1L);
        verify(employeeDao).findById(2L);
        verify(taskDao).addNewEmployeeToTask(1L, 2L);
    }

    @Test
    void testAddNewEmployee_whenEmployeeOrTaskIsNotExist() {
        TaskEmployeeRequest taskEmployeeRequest = new TaskEmployeeRequest();
        taskEmployeeRequest.setTaskId(1L);
        taskEmployeeRequest.setEmployeeId(2L);
        when(taskDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AddNewTaskException.class, () -> taskService.addNewEmployeeToTask(taskEmployeeRequest));
    }

    @Test
    void testDelete_shouldReturnTrue_whenEmployeeExist() {
        Long id = 1L;
        Task task = new Task();
        when(taskDao.findById(id)).thenReturn(Optional.of(task));

        taskService.delete(id);

        verify(taskDao).findById(id);
        verify(taskDao).delete(id);
    }

    @Test
    void testDelete_shouldReturnUpdateException_whenEmployeeIsNotExist() {
        Long id = 1L;
        when(taskDao.findById(id)).thenReturn(Optional.empty());

        assertThrows(UpdateException.class, () -> taskService.delete(id));
    }
}