package domain.dto.mapper;

import domain.dto.response.TaskHistoryResponse;
import domain.entity.Task;
import domain.entity.TaskHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TaskHistoryMapper {

    TaskHistoryMapper INSTANCE = Mappers.getMapper(TaskHistoryMapper.class);

    TaskHistoryResponse taskHistoryToTaskHistoryResponse(TaskHistory taskHistory);




}
