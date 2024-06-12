package ru.belonogov.task_service.domain.dto.mapper;

import org.junit.jupiter.api.Test;
import ru.belonogov.task_service.domain.dto.response.TaskResponse;
import ru.belonogov.task_service.domain.entity.Employee;
import ru.belonogov.task_service.domain.entity.Task;
import ru.belonogov.task_service.domain.entity.TaskStatus;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class TaskMapperTest {

    private TaskMapper taskMapper = TaskMapper.INSTANCE;

    @Test
    void taskToTaskResponse_shouldReturnTaskDto_whenEmployeeExist() {
        Employee employee1 = new Employee();
        employee1.setFirstName("Ivan");
        employee1.setLastName("Ivanov");
        employee1.setRating(5);
        Employee employee2 = new Employee();
        employee2.setFirstName("Vladimir");
        employee2.setLastName("Vladimirov");
        employee2.setRating(5);
        Set<Employee> employees = Set.of(employee1, employee2);
        Task task = new Task();
        task.setName("task1");
        task.setDescription("description for task1");
        task.setRating(5);
        task.setTaskStatus(TaskStatus.IN_PROGRESS);
        task.setEmployees(employees);

        TaskResponse result = taskMapper.taskToTaskResponse(task);

        assertThat(result)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .matches(e -> e.getName().equals(task.getName()))
                .matches(e -> e.getDescription().equals(task.getDescription()))
                .matches(e -> e.getRating() == task.getRating())
                .matches(e -> e.getTaskStatus().equals(task.getTaskStatus().name()))
                .matches(e -> e.getEmployees().size() == 2)
                .matches(e -> e.getEmployees().contains("Ivan Ivanov rating : 5"))
                .matches(e -> e.getEmployees().contains("Vladimir Vladimirov rating : 5"));
    }

    @Test
    void taskToTaskResponse_shouldReturnTaskDto_whenEmployeeIsNotExist() {
        Task task = new Task();
        task.setName("task1");
        task.setDescription("description for task1");
        task.setRating(5);
        task.setTaskStatus(TaskStatus.IN_PROGRESS);
        task.setEmployees(Collections.emptySet());

        TaskResponse result = taskMapper.taskToTaskResponse(task);

        assertThat(result)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .matches(e -> e.getName().equals(task.getName()))
                .matches(e -> e.getDescription().equals(task.getDescription()))
                .matches(e -> e.getRating() == task.getRating())
                .matches(e -> e.getTaskStatus().equals(task.getTaskStatus().name()))
                .matches(e -> e.getEmployees().isEmpty());
    }
}