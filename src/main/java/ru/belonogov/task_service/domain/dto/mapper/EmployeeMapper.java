package ru.belonogov.task_service.domain.dto.mapper;

import org.mapstruct.Named;
import ru.belonogov.task_service.domain.dto.response.EmployeeResponse;
import ru.belonogov.task_service.domain.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.belonogov.task_service.domain.entity.Task;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface EmployeeMapper {

    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    @Mapping(target = "companyName", source = "company.name")
    @Mapping(target = "tasks", expression = "java(buildTaskToString(employee.getTasks()))")
    EmployeeResponse employeeToEmployeeResponse(Employee employee);

    @Named("buildTaskToString")
    public default Set<String> buildTaskToString(Set<Task> tasks) {
        if(tasks.isEmpty()) {
            return Collections.emptySet();
        }

        return tasks.stream()
                .map($ -> $.getName())
                .collect(Collectors.toSet());
    }
}
