package ru.belonogov.task_service.service.impl;

import ru.belonogov.task_service.domain.dto.mapper.TaskMapper;
import ru.belonogov.task_service.domain.dto.request.TaskEmployeeRequest;
import ru.belonogov.task_service.domain.dto.request.TaskRequest;
import ru.belonogov.task_service.domain.dto.request.TaskUpdateRequest;
import ru.belonogov.task_service.domain.dto.response.TaskResponse;
import ru.belonogov.task_service.domain.entity.Task;
import ru.belonogov.task_service.domain.entity.TaskStatus;
import ru.belonogov.task_service.domain.exception.AddNewEmployeeException;
import ru.belonogov.task_service.domain.exception.TaskNotFoundException;
import ru.belonogov.task_service.domain.repository.TaskDao;
import ru.belonogov.task_service.service.TaskService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TaskServiceImpl implements TaskService {

    private final TaskMapper taskMapper = TaskMapper.INSTANCE;
    private final TaskDao taskDao;

    public TaskServiceImpl(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @Override
    public TaskResponse save(TaskRequest taskRequest) {
        TaskStatus newTask = TaskStatus.SEARCH_FOR_EMPLOYEES;
        Task task = new Task();
        task.setName(taskRequest.getName());
        task.setDescription(task.getDescription());
        task.setRating(taskRequest.getRating());
        task.setTaskStatus(newTask);
        task.setEmployees(Collections.emptySet());
        Task save = taskDao.save(task);

        return taskMapper.taskToTaskResponse(save);
    }

    @Override
    public TaskResponse findById(Long id) {
        Task task = taskDao.findById(id).orElseThrow(() -> new TaskNotFoundException("Задание по указанному id не найдено"));

        return taskMapper.taskToTaskResponse(task);
    }

    @Override
    public List<TaskResponse> findAllByEmployee(Long id) {
        List<Task> allByEmployee = taskDao.findAllByEmployee(id);
        List<TaskResponse> result = allByEmployee.stream()
                .map($ -> taskMapper.taskToTaskResponse($))
                .collect(Collectors.toList());

        return result;
    }

    @Override
    public TaskResponse update(TaskUpdateRequest taskUpdateRequest) {
        Task update = taskDao.update(taskUpdateRequest);

        return taskMapper.taskToTaskResponse(update);
    }

    @Override
    public void addNewEmployeeToTask(TaskEmployeeRequest taskEmployeeRequest) {
        Long taskId = taskEmployeeRequest.getTaskId();
        Long employeeId = taskEmployeeRequest.getEmployeeId();
        if(!taskDao.addNewEmployeeToTask(taskId, employeeId)) {
            throw new AddNewEmployeeException("Работник не был назначен на задание");
        }
    }

    @Override
    public void delete(Long id) {
        taskDao.delete(id);
    }
}
