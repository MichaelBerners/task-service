package ru.belonogov.task_service.domain.dto.request;

public class TaskEmployeeRequest {

    private Long taskId;
    private Long employeeId;

    public Long getTaskId() {
        return taskId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}
