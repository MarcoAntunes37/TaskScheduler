package com.taskscheduler.tasksapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import com.taskscheduler.tasksapi.domain.task.NewTaskRequestDto;
import com.taskscheduler.tasksapi.domain.task.Task;

import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {
        @Mappings({
                        @Mapping(target = "id", ignore = true),
                        @Mapping(source = "dto.userId", target = "userId"),
                        @Mapping(source = "dto.title", target = "title"),
                        @Mapping(source = "dto.description", target = "description"),
                        @Mapping(source = "dto.priority", target = "priority"),
                        @Mapping(source = "dto.status", target = "status")
        })
        Task toEntity(NewTaskRequestDto dto);

        @Mappings({
                        @Mapping(source = "entity.userId", target = "userId"),
                        @Mapping(source = "entity.title", target = "title"),
                        @Mapping(source = "entity.description", target = "description"),
                        @Mapping(source = "entity.priority", target = "priority"),
                        @Mapping(source = "entity.status", target = "status")
        })
        NewTaskRequestDto toDto(Task entity);
}
