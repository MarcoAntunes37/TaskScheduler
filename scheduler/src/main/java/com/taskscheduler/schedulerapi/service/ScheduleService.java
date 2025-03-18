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

import com.taskscheduler.schedulerapi.domain.NewScheduleRequestDTO;
import com.taskscheduler.schedulerapi.domain.Schedule;
import com.taskscheduler.schedulerapi.domain.UpdateScheduleRequestDTO;
import com.taskscheduler.schedulerapi.mapper.ScheduleMapper;
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

    public Schedule getScheduleById(UUID id) {
        Schedule schedule = schedulerRepository.findById(id).orElse(null);

        if (schedule == null)
            throw new IllegalArgumentException("Schedule not found");

        return schedule;
    }

    public Schedule createSchedule(NewScheduleRequestDTO schedule) {
        Schedule scheduleEntity = mapper.toEntity(schedule);

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
                () -> new IllegalArgumentException("Schedule not found"));

        existingSchedule.setStartTime(schedule.startTime());

        existingSchedule.setEndTime(schedule.endTime());

        List<Schedule> existingSchedules = schedulerRepository.findAllByUserId(existingSchedule.getUserId());

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
            throw new IllegalArgumentException("Schedule not found");

        schedulerRepository.deleteById(id);
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
            throw new IllegalArgumentException("Schedule not found");

        return schedulerRepository.findAllByUserId(userId);
    }
}