package service;

import domain.dto.request.TaskRequest;
import domain.dto.response.TaskResponse;
import domain.entity.Task;

import java.util.Optional;

public interface TaskService {

    TaskResponse save(TaskRequest taskRequest);

    TaskResponse findById(Long id);

    TaskResponse save(Long id, TaskRequest taskRequest);

    void delete (Long id);
}
