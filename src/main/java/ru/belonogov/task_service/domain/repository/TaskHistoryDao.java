package ru.belonogov.task_service.domain.repository;

import ru.belonogov.task_service.domain.dto.request.TaskHistoryRequest;
import ru.belonogov.task_service.domain.entity.TaskHistory;

import java.util.List;

public interface TaskHistoryDao {

    TaskHistory save(TaskHistoryRequest taskHistoryRequest);

    List<TaskHistory> findByTaskName(String name);

}
