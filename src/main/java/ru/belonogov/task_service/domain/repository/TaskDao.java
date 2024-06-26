package ru.belonogov.task_service.domain.repository;

import ru.belonogov.task_service.domain.dto.request.TaskRequest;
import ru.belonogov.task_service.domain.dto.request.TaskUpdateRequest;
import ru.belonogov.task_service.domain.entity.Task;
import ru.belonogov.task_service.domain.entity.TaskStatus;

import java.util.List;
import java.util.Optional;

public interface TaskDao {

    Task save(Task task);

    Optional<Task> findById(Long id);

    List<Task> findAllByEmployee(Long id);

    Task update(Task task);

    boolean addNewEmployeeToTask(Long taskId, Long employeeId);

    boolean delete (Long id);
}
