package ru.belonogov.task_service.service.impl;

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
import ru.belonogov.task_service.service.TaskService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TaskServiceImpl implements TaskService {

    private final TaskMapper taskMapper;
    private final TaskDao taskDao;
    private final EmployeeDao employeeDao;

    public TaskServiceImpl(TaskMapper taskMapper, TaskDao taskDao, EmployeeDao employeeDao) {
        this.taskMapper = taskMapper;
        this.taskDao = taskDao;
        this.employeeDao = employeeDao;
    }

    @Override
    public TaskResponse save(TaskRequest taskRequest) {
        TaskStatus newTask = TaskStatus.SEARCH_FOR_EMPLOYEES;
        Task task = new Task();
        task.setName(taskRequest.getName());
        task.setDescription(taskRequest.getDescription());
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
        if(allByEmployee.isEmpty()) {
            return Collections.emptyList();
        }
        List<TaskResponse> result = allByEmployee.stream()
                .map($ -> taskMapper.taskToTaskResponse($))
                .collect(Collectors.toList());

        return result;
    }

    @Override
    public TaskResponse update(TaskUpdateRequest taskUpdateRequest) {
        Task task = new Task();
        task.setId(taskUpdateRequest.getId());
        task.setName(taskUpdateRequest.getName());
        task.setDescription(taskUpdateRequest.getDescription());
        task.setRating(taskUpdateRequest.getRating());
        task.setTaskStatus(taskUpdateRequest.getTaskStatus());
        if(taskDao.findById(task.getId()).isEmpty()) {
            throw new UpdateException("Задание не найдено");
        }
        Task update = taskDao.update(task);

        return taskMapper.taskToTaskResponse(update);
    }

    @Override
    public void addNewEmployeeToTask(TaskEmployeeRequest taskEmployeeRequest) {
        Long taskId = taskEmployeeRequest.getTaskId();
        Long employeeId = taskEmployeeRequest.getEmployeeId();
        if(taskDao.findById(taskId).isEmpty() || employeeDao.findById(employeeId).isEmpty()) {
            throw new AddNewTaskException("Ошибка добавления нового задания работнику");
        }
        taskDao.addNewEmployeeToTask(taskId, employeeId);
    }

    @Override
    public void delete(Long id) {
        if (taskDao.findById(id).isEmpty()){
            throw new UpdateException("Задание не найдено");
        }
        taskDao.delete(id);
    }
}
