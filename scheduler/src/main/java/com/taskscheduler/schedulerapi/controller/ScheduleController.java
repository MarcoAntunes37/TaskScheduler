package com.taskscheduler.schedulerapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taskscheduler.schedulerapi.domain.NewScheduleRequestDTO;
import com.taskscheduler.schedulerapi.domain.Schedule;
import com.taskscheduler.schedulerapi.domain.UpdateScheduleRequestDTO;
import com.taskscheduler.schedulerapi.service.ScheduleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/scheduler")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @GetMapping("/{id}")
    public ResponseEntity<Schedule> getSchedule(@PathVariable UUID id) {
        Schedule schedule = scheduleService.getScheduleById(id);

        return ResponseEntity.ok(schedule);
    }

    @GetMapping("/all/{userId}")
    public ResponseEntity<Page<Schedule>> getAllSchedules(
            @PathVariable UUID userId,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sortBy,
            @RequestParam String sortDirection,
            @RequestParam OffsetDateTime startTime,
            @RequestParam OffsetDateTime endTime) {
        Page<Schedule> schedules = scheduleService.getAllSchedules(
                userId, page, size, sortBy, sortDirection, startTime, endTime);

        return ResponseEntity.ok(schedules);
    }

    @PostMapping
    public ResponseEntity<Schedule> createSchedule(@Valid @RequestBody NewScheduleRequestDTO schedule) {
        Schedule savedSchedule = scheduleService.createSchedule(schedule);

        return ResponseEntity.created(
                URI.create("/api/scheduler/" + savedSchedule.getId()))
                .body(savedSchedule);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSchedule(@PathVariable UUID id) {
        scheduleService.deleteScheduleById(id);

        return ResponseEntity.ok("Schedule deleted successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Schedule> updateSchedule(@PathVariable UUID id,
            @RequestBody UpdateScheduleRequestDTO schedule) {
        Schedule updatedSchedule = scheduleService.updateSchedule(id, schedule);

        return ResponseEntity.ok(updatedSchedule);
    }
}
