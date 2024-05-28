package ru.belonogov.task_service.domain.dto.request;

import ru.belonogov.task_service.domain.entity.Task;
import ru.belonogov.task_service.domain.entity.TaskStatus;

import java.time.LocalDate;
import java.time.LocalTime;

public class TaskHistoryRequest {

    private Task task;
    private TaskStatus taskStatus;
    private LocalDate date;
    private LocalTime time;
}
