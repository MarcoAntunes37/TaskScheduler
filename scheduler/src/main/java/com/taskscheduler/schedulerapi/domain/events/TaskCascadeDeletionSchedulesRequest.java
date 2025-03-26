package com.taskscheduler.schedulerapi.domain.events;

import java.util.UUID;

public class TaskCascadeDeletionSchedulesRequest {
    private UUID taskId;

    public TaskCascadeDeletionSchedulesRequest() {
    }

    public TaskCascadeDeletionSchedulesRequest(UUID taskId) {
        this.taskId = taskId;
    }

    public UUID getTaskId() {
        return taskId;
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }
}
