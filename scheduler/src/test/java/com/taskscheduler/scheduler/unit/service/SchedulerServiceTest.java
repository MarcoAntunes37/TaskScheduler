package com.taskscheduler.scheduler.unit.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import com.taskscheduler.schedulerapi.domain.NewScheduleRequestDTO;
import com.taskscheduler.schedulerapi.domain.Schedule;
import com.taskscheduler.schedulerapi.domain.UpdateScheduleRequestDTO;
import com.taskscheduler.schedulerapi.repository.ScheduleRepository;
import com.taskscheduler.schedulerapi.service.ScheduleService;

@ActiveProfiles("test")
public class SchedulerServiceTest {
        @Mock
        private ScheduleRepository repository;

        @InjectMocks
        private ScheduleService schedulerService;

        UUID userId = UUID.randomUUID();

        OffsetDateTime startTimePast = OffsetDateTime
                        .of(2024, 3, 14, 19, 0, 0, 0, ZoneOffset.ofHours(-3));
        OffsetDateTime endTimePast = OffsetDateTime
                        .of(2026, 3, 14, 20, 0, 0, 0, ZoneOffset.ofHours(-3));
        OffsetDateTime startTime2026March14H19M0S0N0Ominus3 = OffsetDateTime
                        .of(2026, 3, 14, 19, 0, 0, 0, ZoneOffset.ofHours(-3));
        OffsetDateTime endTime2026March14H20M0S0N0Ominus3 = OffsetDateTime
                        .of(2026, 3, 14, 20, 0, 0, 0, ZoneOffset.ofHours(-3));
        OffsetDateTime startTime2026March15H19M0S0N0Ominus3 = OffsetDateTime
                        .of(2026, 3, 15, 19, 0, 0, 0, ZoneOffset.ofHours(-3));
        OffsetDateTime endTime2026March15H20M0S0N0Ominus3 = OffsetDateTime
                        .of(2026, 3, 15, 20, 0, 0, 0, ZoneOffset.ofHours(-3));
        OffsetDateTime startTime2026March16H19M0S0N0Ominus3 = OffsetDateTime
                        .of(2026, 3, 16, 19, 0, 0, 0, ZoneOffset.ofHours(-3));
        OffsetDateTime endTime2026March16H20M0S0N0Ominus3 = OffsetDateTime
                        .of(2026, 3, 16, 20, 0, 0, 0, ZoneOffset.ofHours(-3));

        Schedule schedule1 = Schedule.builder()
                        .id(UUID.randomUUID())
                        .userId(userId)
                        .taskId(UUID.randomUUID())
                        .startTime(startTime2026March14H19M0S0N0Ominus3)
                        .endTime(endTime2026March14H20M0S0N0Ominus3)
                        .build();

        Schedule schedule2 = Schedule.builder()
                        .id(UUID.randomUUID())
                        .userId(userId)
                        .taskId(UUID.randomUUID())
                        .startTime(startTime2026March15H19M0S0N0Ominus3)
                        .endTime(endTime2026March15H20M0S0N0Ominus3)
                        .build();
        Schedule schedule3 = Schedule.builder()
                        .id(UUID.randomUUID())
                        .userId(userId)
                        .taskId(UUID.randomUUID())
                        .startTime(startTime2026March16H19M0S0N0Ominus3)
                        .endTime(endTime2026March16H20M0S0N0Ominus3)
                        .build();

        UpdateScheduleRequestDTO updateSchedule = new UpdateScheduleRequestDTO(
                        startTime2026March14H19M0S0N0Ominus3,
                        endTime2026March14H20M0S0N0Ominus3);

        Schedule schedule = new Schedule();

        @BeforeEach
        void setUp() {
                MockitoAnnotations.openMocks(this);
                schedulerService = new ScheduleService(repository);
        }

        @Test
        public void shouldCreateSchedule() {
                NewScheduleRequestDTO newScheduleRequestDTO = new NewScheduleRequestDTO(
                                userId,
                                UUID.randomUUID(),
                                startTime2026March14H19M0S0N0Ominus3,
                                endTime2026March14H20M0S0N0Ominus3);

                when(repository.save(any(Schedule.class))).thenReturn(schedule);

                schedulerService.createSchedule(newScheduleRequestDTO);

                verify(repository, times(1)).save(any(Schedule.class));
        }

        @Test
        public void shouldFailToCreateScheduleWhenUserIdIsNull() {
                NewScheduleRequestDTO newScheduleRequestDTO = new NewScheduleRequestDTO(
                                null,
                                UUID.randomUUID(),
                                startTimePast,
                                endTime2026March14H20M0S0N0Ominus3);

                when(repository.save(any(Schedule.class))).thenThrow(
                                new RuntimeException("User ID is required"));

                assertThrows(RuntimeException.class, () -> schedulerService.createSchedule(newScheduleRequestDTO));
        }

        @Test
        public void shouldFailToCreateScheduleWhenTakIdIsNull() {
                NewScheduleRequestDTO newScheduleRequestDTO = new NewScheduleRequestDTO(
                                userId,
                                null,
                                startTimePast,
                                endTime2026March14H20M0S0N0Ominus3);

                when(repository.save(any(Schedule.class))).thenThrow(
                                new RuntimeException("Task ID is required"));

                assertThrows(RuntimeException.class, () -> schedulerService.createSchedule(newScheduleRequestDTO));
        }

        @Test
        public void shouldFailToCreateScheduleWhenStartTimeIsNull() {
                NewScheduleRequestDTO newScheduleRequestDTO = new NewScheduleRequestDTO(
                                userId,
                                UUID.randomUUID(),
                                null,
                                endTime2026March14H20M0S0N0Ominus3);

                when(repository.save(any(Schedule.class))).thenThrow(
                                new RuntimeException("Start time ise required"));

                assertThrows(RuntimeException.class, () -> schedulerService.createSchedule(newScheduleRequestDTO));
        }

        @Test
        public void shouldFailToCreateScheduleWhenStartTimeIsPast() {
                NewScheduleRequestDTO newScheduleRequestDTO = new NewScheduleRequestDTO(
                                userId,
                                UUID.randomUUID(),
                                startTimePast,
                                endTime2026March14H20M0S0N0Ominus3);

                when(repository.save(any(Schedule.class))).thenThrow(
                                new RuntimeException("Start time must be in the future"));

                assertThrows(RuntimeException.class, () -> schedulerService.createSchedule(newScheduleRequestDTO));
        }

        @Test
        public void shouldFailToCreateScheduleWhenEndTimeIsNull() {
                NewScheduleRequestDTO newScheduleRequestDTO = new NewScheduleRequestDTO(
                                userId,
                                UUID.randomUUID(),
                                startTime2026March14H19M0S0N0Ominus3,
                                null);

                when(repository.save(any(Schedule.class))).thenThrow(
                                new RuntimeException("End time is required"));

                assertThrows(RuntimeException.class, () -> schedulerService.createSchedule(newScheduleRequestDTO));
        }

        @Test
        public void shouldFailToCreateScheduleWhenEndTimeIsPast() {
                NewScheduleRequestDTO newScheduleRequestDTO = new NewScheduleRequestDTO(
                                userId,
                                UUID.randomUUID(),
                                startTime2026March14H19M0S0N0Ominus3,
                                endTimePast);

                when(repository.save(any(Schedule.class))).thenThrow(
                                new RuntimeException("End time must be in the future"));

                assertThrows(RuntimeException.class, () -> schedulerService.createSchedule(newScheduleRequestDTO));
        }

        @Test
        public void shouldFailToCreateScheduleWhenEndTimeIsBeforeStartTime() {
                NewScheduleRequestDTO newScheduleRequestDTO = new NewScheduleRequestDTO(
                                userId,
                                UUID.randomUUID(),
                                endTime2026March14H20M0S0N0Ominus3,
                                startTime2026March14H19M0S0N0Ominus3);

                when(repository.save(any(Schedule.class))).thenThrow(
                                new IllegalArgumentException("Start time must be before end time"));

                assertThrows(IllegalArgumentException.class,
                                () -> schedulerService.createSchedule(newScheduleRequestDTO));
        }

        @Test
        public void shouldFailToCreateScheduleWhenStartTimeAndEndTimeAreSame() {
                NewScheduleRequestDTO newScheduleRequestDTO = new NewScheduleRequestDTO(
                                userId,
                                UUID.randomUUID(),
                                startTime2026March14H19M0S0N0Ominus3,
                                startTime2026March14H19M0S0N0Ominus3);

                when(repository.save(any(Schedule.class))).thenThrow(
                                new IllegalArgumentException("Start time and end time must be different"));

                assertThrows(IllegalArgumentException.class,
                                () -> schedulerService.createSchedule(newScheduleRequestDTO));
        }

        @Test
        public void shouldFailToCreateScheduleWhenScheduleOverlapAnother() {
                NewScheduleRequestDTO newScheduleRequestDTO = new NewScheduleRequestDTO(
                                userId,
                                UUID.randomUUID(),
                                endTime2026March14H20M0S0N0Ominus3,
                                startTime2026March14H19M0S0N0Ominus3);

                when(repository.save(any(Schedule.class))).thenThrow(
                                new IllegalArgumentException("Schedule overlaps with existing schedule"));

                assertThrows(IllegalArgumentException.class,
                                () -> schedulerService.createSchedule(newScheduleRequestDTO));
        }

        @Test
        public void shouldUpdateSchedule() {
                when(repository.findById(schedule1.getId())).thenReturn(Optional.of(schedule1));
                schedulerService.updateSchedule(schedule1.getId(), updateSchedule);
                verify(repository, times(1)).save(any(Schedule.class));
        }

        @Test
        public void shouldFailUpdateScheduleWhenStartTimeIsNull() {
                UpdateScheduleRequestDTO updateSchedule = new UpdateScheduleRequestDTO(
                                null,
                                endTime2026March14H20M0S0N0Ominus3);

                when(repository.save(any(Schedule.class))).thenThrow(
                                new RuntimeException("Start time is required"));

                assertThrows(RuntimeException.class,
                                () -> schedulerService.updateSchedule(schedule1.getId(), updateSchedule));
        }

        @Test
        public void shouldFailUpdateScheduleWhenStartTimeIsPast() {
                UpdateScheduleRequestDTO updateSchedule = new UpdateScheduleRequestDTO(
                                startTimePast,
                                endTime2026March14H20M0S0N0Ominus3);

                when(repository.save(any(Schedule.class))).thenThrow(
                                new RuntimeException("Start time must be in the future"));

                assertThrows(RuntimeException.class,
                                () -> schedulerService.updateSchedule(schedule1.getId(), updateSchedule));
        }

        @Test
        public void shouldFailUpdateScheduleWhenEndTimeIsNull() {
                UpdateScheduleRequestDTO updateSchedule = new UpdateScheduleRequestDTO(
                                startTime2026March14H19M0S0N0Ominus3,
                                null);

                when(repository.save(any(Schedule.class))).thenThrow(
                                new RuntimeException("End time is required"));

                assertThrows(RuntimeException.class,
                                () -> schedulerService.updateSchedule(schedule1.getId(), updateSchedule));
        }

        @Test
        public void shouldFailUpdateScheduleWhenEndTimeIsPast() {
                UpdateScheduleRequestDTO updateSchedule = new UpdateScheduleRequestDTO(
                                startTime2026March14H19M0S0N0Ominus3,
                                endTimePast);

                when(repository.save(any(Schedule.class))).thenThrow(
                                new RuntimeException("Start time must be in the future"));

                assertThrows(RuntimeException.class,
                                () -> schedulerService.updateSchedule(schedule1.getId(), updateSchedule));
        }

        @Test
        public void shouldFailToUpdateScheduleWhenEndTimeIsBeforeStartTime() {
                UpdateScheduleRequestDTO UpdateScheduleRequestDTO = new UpdateScheduleRequestDTO(
                                endTime2026March14H20M0S0N0Ominus3,
                                startTime2026March14H19M0S0N0Ominus3);

                when(repository.save(any(Schedule.class))).thenThrow(
                                new IllegalArgumentException("Start time must be before end time"));

                assertThrows(IllegalArgumentException.class,
                                () -> schedulerService.updateSchedule(schedule1.getId(), UpdateScheduleRequestDTO));
        }

        @Test
        public void shouldFailToUpdateScheduleWhenStartTimeAndEndTimeAreSame() {
                NewScheduleRequestDTO newScheduleRequestDTO = new NewScheduleRequestDTO(
                                userId,
                                UUID.randomUUID(),
                                startTime2026March14H19M0S0N0Ominus3,
                                startTime2026March14H19M0S0N0Ominus3);

                when(repository.save(any(Schedule.class))).thenThrow(
                                new IllegalArgumentException("Start time and end time must be different"));

                assertThrows(IllegalArgumentException.class,
                                () -> schedulerService.createSchedule(newScheduleRequestDTO));
        }

        @Test
        public void shouldFailToUpdateScheduleWhenScheduleOverlapAnother() {
                NewScheduleRequestDTO newScheduleRequestDTO = new NewScheduleRequestDTO(
                                userId,
                                UUID.randomUUID(),
                                endTime2026March14H20M0S0N0Ominus3,
                                startTime2026March14H19M0S0N0Ominus3);

                when(repository.save(any(Schedule.class))).thenThrow(
                                new IllegalArgumentException("Schedule overlaps with existing schedule"));

                assertThrows(IllegalArgumentException.class,
                                () -> schedulerService.createSchedule(newScheduleRequestDTO));
        }

        @Test
        public void shouldDeleteSchedule() {
                schedulerService.deleteScheduleById(schedule1.getId());
                verify(repository, times(1)).deleteById(schedule1.getId());
        }

        @Test
        public void shouldFailToDeleteScheduleWhenIdNotFound() {
                when(repository.findById(schedule1.getId())).thenReturn(Optional.empty());
                assertThrows(IllegalArgumentException.class,
                                () -> schedulerService.deleteScheduleById(schedule1.getId()));
        }

        @Test
        public void shouldGetAllSchedules() {
                Page<Schedule> page = new PageImpl<>(List.of(schedule1, schedule2, schedule3));

                when(schedulerService.getAllSchedules(userId, 0, 10, "id", "asc", startTime2026March14H19M0S0N0Ominus3,
                                startTime2026March16H19M0S0N0Ominus3)).thenReturn(page);

                schedulerService.getAllSchedules(userId, 0, 10, "id", "asc", startTime2026March14H19M0S0N0Ominus3,
                                endTime2026March16H20M0S0N0Ominus3);

                verify(repository).findAllByUserId(userId, startTime2026March14H19M0S0N0Ominus3,
                                endTime2026March16H20M0S0N0Ominus3, PageRequest.of(0, 10, Sort.by("id").ascending()));
        }

        @Test
        public void shouldGetAllSchedulesEmpty() {
                Page<Schedule> page = new PageImpl<>(List.of());

                when(schedulerService.getAllSchedules(userId, 0, 10, "id", "asc", startTime2026March14H19M0S0N0Ominus3,
                                startTime2026March16H19M0S0N0Ominus3)).thenReturn(page);

                schedulerService.getAllSchedules(userId, 0, 10, "id", "asc", startTime2026March14H19M0S0N0Ominus3,
                                endTime2026March16H20M0S0N0Ominus3);

                verify(repository).findAllByUserId(userId, startTime2026March14H19M0S0N0Ominus3,
                                endTime2026March16H20M0S0N0Ominus3, PageRequest.of(0, 10, Sort.by("id").ascending()));
        }

        @Test
        public void shouldGetScheduleById() {
                when(repository.findById(schedule1.getId())).thenReturn(Optional.of(schedule1));
                schedulerService.getScheduleById(schedule1.getId());
                verify(repository).findById(schedule1.getId());
        }

        @Test
        public void shouldNotGetTaskById() {
                when(repository.findById(UUID.randomUUID())).thenReturn(Optional.empty());
                assertThrows(IllegalArgumentException.class, () -> schedulerService.getScheduleById(schedule1.getId()));
                verify(repository).findById(schedule1.getId());
        }
}