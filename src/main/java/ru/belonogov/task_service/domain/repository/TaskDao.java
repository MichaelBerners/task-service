package ru.belonogov.task_service.domain.repository;

import ru.belonogov.task_service.domain.dto.request.TaskRequest;
import ru.belonogov.task_service.domain.dto.request.TaskUpdateRequest;
import ru.belonogov.task_service.domain.entity.Task;

import java.util.List;
import java.util.Optional;

public interface TaskDao {

    Task save(TaskRequest taskRequest);

    Optional<Task> findById(Long id);

    List<Task> findAllByEmployee(Long id);

    Task update(TaskUpdateRequest taskUpdateRequest);

    boolean addNewEmployeeToTask(Long taskId, Long employeeId);

    void delete (Long id);
}
