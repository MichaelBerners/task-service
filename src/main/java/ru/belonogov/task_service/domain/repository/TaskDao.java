package ru.belonogov.task_service.domain.repository;

import ru.belonogov.task_service.domain.dto.request.TaskRequest;
import ru.belonogov.task_service.domain.entity.Task;

import java.util.Optional;

public interface TaskDao {

    Task save(TaskRequest taskRequest);

    Optional<Task> findById(Long id);

    Task save(Long id, TaskRequest taskRequest);

    void delete (Long id);
}
