package ru.belonogov.task_service.domain.dto.mapper;

import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.belonogov.task_service.domain.dto.response.TaskResponse;
import ru.belonogov.task_service.domain.entity.Employee;
import ru.belonogov.task_service.domain.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface TaskMapper {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    @Mapping(target = "employees", expression = "java(buildEmployeesToString(task.getEmployees()))")
    TaskResponse taskToTaskResponse(Task task);

    @Named("buildEmployeesToString")
    default Set<String> buildEmployeesToString(Set<Employee> employees) {
        if(employees.isEmpty()) {

            return Collections.emptySet();
        }

        return employees.stream()
                .map($ -> ($.getFirstName() + " " + $.getLastName() + " rating : " + $.getRating()))
                .collect(Collectors.toSet());
    }
}
