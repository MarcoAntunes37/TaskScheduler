package com.taskscheduler.schedulerapi.domain.events;

import java.util.UUID;

public class TaskExistsRequest {
    private UUID taskId;

    public TaskExistsRequest() {
    }

    public TaskExistsRequest(UUID taskId) {
        this.taskId = taskId;
    }

    public UUID getTaskId() {
        return taskId;
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }
}