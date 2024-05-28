package domain.dto.mapper;

import domain.dto.response.TaskResponse;
import domain.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TaskMapper {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    TaskResponse taskToTaskResponse(Task task);
}
