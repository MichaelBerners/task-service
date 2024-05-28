package ru.belonogov.task_service.service;

import ru.belonogov.task_service.domain.dto.request.TaskHistoryRequest;
import ru.belonogov.task_service.domain.dto.response.TaskHistoryResponse;

public interface TaskHistoryService {

    TaskHistoryResponse save(TaskHistoryRequest taskHistoryRequest);

    TaskHistoryRequest findByTaskName(String name);
}
