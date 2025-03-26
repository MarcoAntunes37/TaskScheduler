package com.taskscheduler.schedulerapi.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taskscheduler.schedulerapi.domain.events.TaskExistsRequest;
import com.taskscheduler.schedulerapi.domain.schedule.NewScheduleRequestDTO;
import com.taskscheduler.schedulerapi.domain.schedule.Schedule;
import com.taskscheduler.schedulerapi.domain.schedule.UpdateScheduleRequestDTO;
import com.taskscheduler.schedulerapi.mapper.ScheduleMapper;
import com.taskscheduler.schedulerapi.producers.TaskExistsProducer;
import com.taskscheduler.schedulerapi.repository.ScheduleRepository;
import com.taskscheduler.schedulerapi.util.ScheduleUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository schedulerRepository;

    @Autowired
    private ScheduleMapper mapper = Mappers.getMapper(ScheduleMapper.class);

    @Autowired
    private TaskExistsProducer taskProducer;

    public Schedule getScheduleById(UUID id) {
        Schedule schedule = schedulerRepository.findById(id).orElse(null);

        if (schedule == null)
            throw new IllegalArgumentException("Schedule not found.");

        return schedule;
    }

    public Schedule createSchedule(NewScheduleRequestDTO schedule) {
        Schedule scheduleEntity = mapper.toEntity(schedule);

        TaskExistsRequest taskExistsRequest = new TaskExistsRequest(scheduleEntity.getTaskId());

        boolean taskExists = taskProducer.checkTaskExists(taskExistsRequest);

        if (!taskExists) {
            throw new IllegalArgumentException("Task not found.");
        }

        List<Schedule> existingSchedules = schedulerRepository.findAllByUserId(scheduleEntity.getUserId());

        ScheduleUtil.validateScheduleRoutine(scheduleEntity, existingSchedules);

        try {
            schedulerRepository.save(scheduleEntity);
            return scheduleEntity;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public Schedule updateSchedule(UUID id, UpdateScheduleRequestDTO schedule) {
        Schedule existingSchedule = schedulerRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Schedule not found."));

        TaskExistsRequest taskExistsRequest = new TaskExistsRequest(schedule.taskId());

        boolean taskExists = taskProducer.checkTaskExists(taskExistsRequest);

        if (!taskExists) {
            throw new IllegalArgumentException("Task not found.");
        }

        existingSchedule.setTaskId(schedule.taskId());

        existingSchedule.setStartTime(schedule.startTime());

        existingSchedule.setEndTime(schedule.endTime());

        List<Schedule> existingSchedules = schedulerRepository.findAllByUserId(existingSchedule.getUserId());

        existingSchedules.removeIf(s -> s.getId().equals(id));

        ScheduleUtil.validateScheduleRoutine(existingSchedule, existingSchedules);

        try {
            schedulerRepository.save(existingSchedule);

            return existingSchedule;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void deleteScheduleById(UUID id) {
        if (schedulerRepository.findById(id).isEmpty())
            throw new IllegalArgumentException("Schedule not found.");

        schedulerRepository.deleteById(id);
    }

    
    @Transactional
    public Integer deleteAllSchedulesByTaskId(UUID taskId) {
        Integer response = schedulerRepository.deleteAllByTaskId(taskId);

        return response;
    }

    public Page<Schedule> getAllSchedules(
            UUID userId, int page, int size, String sortBy, String sortDirection,
            OffsetDateTime startTime, OffsetDateTime endTime) {
        Sort sort = sortDirection.equals("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        PageRequest pageable = PageRequest.of(page, size, sort);

        return schedulerRepository.findAllByUserId(userId, startTime, endTime, pageable);
    }

    public List<Schedule> getAllSchedulesByUserId(UUID userId) {
        Schedule schedule = schedulerRepository.findById(userId).orElse(null);

        if (schedule == null)
            throw new IllegalArgumentException("Schedule not found.");

        return schedulerRepository.findAllByUserId(userId);
    }
}