package com.taskscheduler.schedulerapi.domain;

public class TaskExistsResponse {
    private boolean exists;

    public TaskExistsResponse() {
    }

    public TaskExistsResponse(boolean exists) {
        this.exists = exists;
    }

    public boolean getExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }
}