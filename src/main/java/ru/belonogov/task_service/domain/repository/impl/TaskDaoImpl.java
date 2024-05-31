package ru.belonogov.task_service.domain.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.belonogov.task_service.domain.dto.request.TaskRequest;
import ru.belonogov.task_service.domain.dto.request.TaskUpdateRequest;
import ru.belonogov.task_service.domain.entity.Task;
import ru.belonogov.task_service.domain.repository.TaskDao;
import ru.belonogov.task_service.util.MyConnectionPool;

import java.util.List;
import java.util.Optional;

public class TaskDaoImpl implements TaskDao {

    private final MyConnectionPool myConnectionPool = new MyConnectionPool();
    private final Logger logger = LoggerFactory.getLogger(TaskDaoImpl.class);
    @Override
    public Task save(TaskRequest taskRequest) {
        return null;
    }

    @Override
    public Optional<Task> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Task> findAllByEmployee(Long id) {
        return List.of();
    }

    @Override
    public Task update(TaskUpdateRequest taskUpdateRequest) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
