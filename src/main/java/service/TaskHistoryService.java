package service;

import domain.dto.request.TaskHistoryRequest;
import domain.dto.response.TaskHistoryResponse;
import domain.entity.Task;

import java.util.List;

public interface TaskHistoryService {

    TaskHistoryResponse save(TaskHistoryRequest taskHistoryRequest);

    TaskHistoryRequest findByTaskName(String name);
}
