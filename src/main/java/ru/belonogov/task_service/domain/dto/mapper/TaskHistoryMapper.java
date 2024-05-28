package ru.belonogov.task_service.domain.dto.mapper;

import ru.belonogov.task_service.domain.dto.response.TaskHistoryResponse;
import ru.belonogov.task_service.domain.entity.TaskHistory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TaskHistoryMapper {

    TaskHistoryMapper INSTANCE = Mappers.getMapper(TaskHistoryMapper.class);

    TaskHistoryResponse taskHistoryToTaskHistoryResponse(TaskHistory taskHistory);




}
