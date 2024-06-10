package ru.belonogov.task_service.service;

import ru.belonogov.task_service.domain.dto.request.TaskRequest;
import ru.belonogov.task_service.domain.dto.request.TaskUpdateRequest;
import ru.belonogov.task_service.domain.dto.request.TaskEmployeeRequest;
import ru.belonogov.task_service.domain.dto.response.TaskResponse;
import ru.belonogov.task_service.domain.entity.Task;

import java.util.List;

public interface TaskService {

    TaskResponse save(TaskRequest taskRequest);

    TaskResponse findById(Long id);

    List<TaskResponse> findAllByEmployee(Long id);

    TaskResponse update(TaskUpdateRequest taskUpdateRequest);

    void addNewEmployeeToTask(TaskEmployeeRequest taskEmployeeRequest);

    boolean delete (Long id);
}
