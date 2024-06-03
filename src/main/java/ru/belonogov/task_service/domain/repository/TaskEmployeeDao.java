package ru.belonogov.task_service.domain.repository;

import ru.belonogov.task_service.domain.entity.Task;

public interface TaskEmployeeDao {

    boolean save (Long taskId, Long employeeId);
}
