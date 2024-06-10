package ru.belonogov.task_service.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import ru.belonogov.task_service.domain.dto.mapper.TaskMapper;
import ru.belonogov.task_service.domain.dto.request.TaskEmployeeRequest;
import ru.belonogov.task_service.domain.dto.request.TaskRequest;
import ru.belonogov.task_service.domain.dto.request.TaskUpdateRequest;
import ru.belonogov.task_service.domain.dto.response.TaskResponse;
import ru.belonogov.task_service.domain.entity.Task;
import ru.belonogov.task_service.domain.entity.TaskStatus;
import ru.belonogov.task_service.domain.exception.AddNewTaskException;
import ru.belonogov.task_service.domain.exception.TaskNotFoundException;
import ru.belonogov.task_service.domain.exception.UpdateException;
import ru.belonogov.task_service.domain.repository.EmployeeDao;
import ru.belonogov.task_service.domain.repository.TaskDao;
import ru.belonogov.task_service.domain.repository.impl.EmployeeDaoImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceImplTest {

    @Mock
    private TaskMapper taskMapper;
    @Mock
    private TaskDao taskDao;
    private EmployeeDao employeeDao;
    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void testSave_shouldTaskDTO_whenTaskSaved() {
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setName("Новое задание");
        taskRequest.setDescription("Описание нового задания");
        taskRequest.setRating(7);
        when(taskDao.save(argThat(arg -> {
            assertThat(arg.getName().equals("Новое задание")).isTrue();
            assertThat(arg.getDescription().equals("Описание нового задания")).isTrue();
            assertThat(arg.getRating() == 7).isTrue();
            assertThat(arg.getTaskStatus() == TaskStatus.SEARCH_FOR_EMPLOYEES).isTrue();
            return true;
        }))).thenAnswer(e -> e.getArgument(0));
        when(taskMapper.taskToTaskResponse(argThat(arg -> {
            assertThat(arg.getName().equals("Новое задание")).isTrue();
            assertThat(arg.getDescription().equals("Описание нового задания")).isTrue();
            assertThat(arg.getRating() == 7).isTrue();
            assertThat(arg.getTaskStatus() == TaskStatus.SEARCH_FOR_EMPLOYEES).isTrue();
            return true;
        }))).thenReturn(mock(TaskResponse.class));

        taskService.save(taskRequest);

        verify(taskDao).save(any());
        verify(taskMapper).taskToTaskResponse(any());
    }

    @Test
    void testFindById_shouldReturnTaskDTO_whenTaskExist() {
        Long id = 1L;
        Task task = new Task();
        task.setId(id);
        when(taskDao.findById(id)).thenReturn(Optional.ofNullable(task));
        when(taskMapper.taskToTaskResponse(task)).thenReturn(mock(TaskResponse.class));

        taskService.findById(id);

        verify(taskDao).findById(id);
        verify(taskMapper).taskToTaskResponse(task);
    }

    @Test
    void testFindById_shouldReturnTaskNotFoundException_whenTaskIsNotExist() {
        Long id = 1L;
        Task task = new Task();
        task.setId(id);
        when(taskDao.findById(id)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.findById(id));

        verify(taskDao).findById(id);
    }

    @Test
    void testFindAllByEmployee_shouldReturnListTaskDAO_whenEmployeeExist() {
        Long id = 1L;
        when(taskDao.findAllByEmployee(id)).thenReturn(mock(List.class));
        when(taskMapper.taskToTaskResponse(mock(Task.class)));

        taskDao.findAllByEmployee(id);

        verify(taskDao).findAllByEmployee(id);
        verify(taskMapper).taskToTaskResponse(any());
    }

    @Test
    void testFindAllByEmployee_shouldReturnEmptyList_whenEmployeeIsNotExist() {
        Long id = 1L;
        when(taskDao.findAllByEmployee(id)).thenReturn(Collections.emptyList());

        taskDao.findAllByEmployee(id);

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
        task.setId(taskUpdateRequest.getId());
        when(taskDao.findById(1L)).thenReturn(Optional.ofNullable(task));
        when(taskDao.update(task)).thenReturn(mock(Task.class));
        when(taskMapper.taskToTaskResponse(any()));

        taskService.update(taskUpdateRequest);

        verify(taskDao).findById(1L);
        verify(taskDao).update(task);
        verify(taskMapper).taskToTaskResponse(any());
    }

    @Test
    void taskUpdate_shouldReturnUpdateException_whenTaskExist() {
        TaskUpdateRequest taskUpdateRequest = new TaskUpdateRequest();
        taskUpdateRequest.setId(1L);
        taskUpdateRequest.setName("Старое название");
        taskUpdateRequest.setDescription("Новое описание");
        taskUpdateRequest.setRating(5);
        taskUpdateRequest.setTaskStatus(TaskStatus.IN_PROGRESS);
        Task task = new Task();
        task.setId(taskUpdateRequest.getId());
        when(taskDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UpdateException.class, () -> taskService.update(taskUpdateRequest));
    }

    @Test
    void testAddNewEmployee_whenEmployeeAndTaskExist() {
        TaskEmployeeRequest taskEmployeeRequest = new TaskEmployeeRequest();
        taskEmployeeRequest.setTaskId(1L);
        taskEmployeeRequest.setEmployeeId(2L);
        when(taskDao.findById(1L)).thenReturn(mock(Optional.class));
        when(employeeDao.findById(2L)).thenReturn(mock(Optional.class));

        taskService.addNewEmployeeToTask(taskEmployeeRequest);

        verify(taskDao.findById(1L));
        verify(employeeDao.findById(2L));
        verify(employeeDao.addNewTask(1L, 2L));
    }

    @Test
    void testAddNewEmployee_whenEmployeeOrTaskIsNotExist() {
        TaskEmployeeRequest taskEmployeeRequest = new TaskEmployeeRequest();
        taskEmployeeRequest.setTaskId(1L);
        taskEmployeeRequest.setEmployeeId(2L);
        when(taskDao.findById(1L)).thenReturn(Optional.empty());
        when(employeeDao.findById(2L)).thenReturn(mock(Optional.class));

        assertThrows(AddNewTaskException.class, () -> taskService.addNewEmployeeToTask(taskEmployeeRequest));
    }

    @Test
    void testDelete_shouldReturnTrue_whenEmployeeExist() {
        Long id = 1L;
        when(taskDao.findById(id)).thenReturn(mock(Optional.class));

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