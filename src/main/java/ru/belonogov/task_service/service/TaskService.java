package ru.belonogov.task_service.service;

import ru.belonogov.task_service.domain.dto.request.TaskRequest;
import ru.belonogov.task_service.domain.dto.response.TaskResponse;

public interface TaskService {

    TaskResponse save(TaskRequest taskRequest);

    TaskResponse findById(Long id);

    TaskResponse save(Long id, TaskRequest taskRequest);

    void delete (Long id);
}
