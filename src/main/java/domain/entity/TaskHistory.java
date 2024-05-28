package domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class TaskHistory {

    private Long id;
    private List<Task> tasks;
    private TaskStatus taskStatus;
    private LocalDate date;
    private LocalTime localTime;
}
