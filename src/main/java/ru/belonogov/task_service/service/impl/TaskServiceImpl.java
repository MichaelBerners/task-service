package ru.belonogov.task_service.service.impl;

import ru.belonogov.task_service.domain.dto.mapper.TaskMapper;
import ru.belonogov.task_service.domain.dto.request.TaskRequest;
import ru.belonogov.task_service.domain.dto.request.TaskUpdateRequest;
import ru.belonogov.task_service.domain.dto.response.TaskResponse;
import ru.belonogov.task_service.domain.entity.Task;
import ru.belonogov.task_service.domain.entity.TaskStatus;
import ru.belonogov.task_service.domain.exception.TaskNotFoundException;
import ru.belonogov.task_service.domain.repository.TaskDao;
import ru.belonogov.task_service.service.TaskService;

public class TaskServiceImpl implements TaskService {

    private final TaskMapper taskMapper = TaskMapper.INSTANCE;
    private final TaskDao taskDao;

    public TaskServiceImpl(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @Override
    public TaskResponse save(TaskRequest taskRequest) {
        TaskStatus newTask = TaskStatus.SEARCH_FOR_EMPLOYEES;
        Task save = taskDao.save(taskRequest, newTask);

        return taskMapper.taskToTaskResponse(save);
    }

    @Override
    public TaskResponse findById(Long id) {
        Task task = taskDao.findById(id).orElseThrow(() -> new TaskNotFoundException("Задание по указанному id не найдено"));

        return taskMapper.taskToTaskResponse(task);
    }

    @Override
    public TaskResponse update(TaskUpdateRequest taskUpdateRequest) {
        Task update = taskDao.update(taskUpdateRequest);

        return taskMapper.taskToTaskResponse(update);
    }

    @Override
    public void addNewEmployeeToTask(Long taskId, Long employeeId) {
        if(!taskDao.addNewEmployeeToTask(taskId, employeeId)) {
            throw new RuntimeException("Работник не был назначен на задание");
        }

    }

    @Override
    public void delete(Long id) {
        taskDao.delete(id);
    }
}
