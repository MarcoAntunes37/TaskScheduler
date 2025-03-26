package com.taskscheduler.schedulerapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.taskscheduler.schedulerapi.domain.schedule.NewScheduleRequestDTO;
import com.taskscheduler.schedulerapi.domain.schedule.Schedule;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "userId", source = "userId"),
            @Mapping(target = "taskId", source = "taskId"),
            @Mapping(target = "startTime", source = "startTime"),
            @Mapping(target = "endTime", source = "endTime")
    })
    Schedule toEntity(NewScheduleRequestDTO dto);
}