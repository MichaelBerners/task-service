package domain.repository;

import domain.dto.request.TaskHistoryRequest;
import domain.entity.Task;
import domain.entity.TaskHistory;

import java.util.List;

public interface TaskHistoryDao {

    TaskHistory save(TaskHistoryRequest taskHistoryRequest);

    List<TaskHistory> findByTaskName(String name);

}
