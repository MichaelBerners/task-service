package domain.dto.response;

import domain.entity.Task;
import domain.entity.TaskStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TaskHistoryResponse {

    private String taskName;
    private String taskStatus;
    private LocalDate date;
    private LocalTime localTime;
}
