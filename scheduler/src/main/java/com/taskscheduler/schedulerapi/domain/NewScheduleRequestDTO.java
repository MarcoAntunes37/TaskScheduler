package com.taskscheduler.schedulerapi.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public record NewScheduleRequestDTO(
        @NotNull(message = "User ID is required") UUID userId,
        @NotNull(message = "Task ID is required") UUID taskId,
        @NotNull(message = "Start time is required") @Future(message = "Start time must be in the future") OffsetDateTime startTime,
        @NotNull(message = "End time is required") @Future(message = "End time must be in the future") OffsetDateTime endTime) {
}