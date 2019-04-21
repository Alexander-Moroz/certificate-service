package com.amoroz.model;

public enum TaskStatus {
    CREATED(1),
    INPROGRESS(2),
    ERROR(3),
    CLOSED(4);

    private int status;

    TaskStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public static TaskStatus getStatus(int value) {
        for (TaskStatus taskStatus : TaskStatus.values()) {
            if (taskStatus.status == value) return taskStatus;
        }
        return null;
    }
}