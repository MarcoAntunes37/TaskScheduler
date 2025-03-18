package com.taskscheduler.schedulerapi.util;

import java.util.List;

import com.taskscheduler.schedulerapi.domain.Schedule;

public final class ScheduleUtil {
    private ScheduleUtil() {
        throw new IllegalStateException("Cannot be instantiated");
    }

    public static void validateScheduleRoutine(Schedule scheduleEntity, List<Schedule> existingSchedules) {
        validateScheduleStartTimeIsBeforeEndTime(scheduleEntity);
        validateScheduleStartTimeEndTimeIsEqual(scheduleEntity);
        validateScheduleOverlap(scheduleEntity, existingSchedules);
    }

    private static void validateScheduleOverlap(Schedule scheduleEntity, List<Schedule> existingSchedules) {
        for (Schedule existingSchedule : existingSchedules) {
            boolean overlaps = (scheduleEntity.getStartTime()
                    .isBefore(existingSchedule.getEndTime())
                    && scheduleEntity.getEndTime()
                            .isAfter(existingSchedule.getStartTime()));

            if (overlaps) {
                throw new IllegalArgumentException("Schedule overlaps with existing schedule");
            }
        }
    }

    private static void validateScheduleStartTimeEndTimeIsEqual(Schedule scheduleEntity) {
        if (scheduleEntity.getStartTime().equals(scheduleEntity.getEndTime())) {
            throw new IllegalArgumentException("Start time and end time must be different");
        }
    }

    private static void validateScheduleStartTimeIsBeforeEndTime(Schedule scheduleEntity) {
        if (scheduleEntity.getStartTime().isAfter(scheduleEntity.getEndTime())) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
    }
}