package domain.dto.request;

import domain.entity.Task;
import domain.entity.TaskStatus;

import java.time.LocalDate;
import java.time.LocalTime;

public class TaskHistoryRequest {

    private Task task;
    private TaskStatus taskStatus;
    private LocalDate date;
    private LocalTime time;
}
