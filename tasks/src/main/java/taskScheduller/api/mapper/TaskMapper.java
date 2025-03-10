package taskScheduller.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import taskScheduller.api.domain.task.NewTaskRequestDto;
import taskScheduller.api.domain.task.Task;

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

}
