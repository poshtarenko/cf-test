package com.poshtarenko.codeforge.dto.mapper;

import com.poshtarenko.codeforge.dto.request.CreateTaskDTO;
import com.poshtarenko.codeforge.dto.request.UpdateTaskDTO;
import com.poshtarenko.codeforge.dto.response.ViewTaskDTO;
import com.poshtarenko.codeforge.entity.test.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {EntityIdMapper.class})
public interface TaskMapper {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    @Mapping(source = "problem", target = "problemId")
    @Mapping(source = "test", target = "testId")
    ViewTaskDTO toDto(Task entity);

    @Mapping(source = "problemId", target = "problem")
    @Mapping(source = "testId", target = "test")
    Task toEntity(CreateTaskDTO dto);

    @Mapping(source = "problemId", target = "problem")
    @Mapping(source = "testId", target = "test")
    Task toEntity(UpdateTaskDTO dto);

}
