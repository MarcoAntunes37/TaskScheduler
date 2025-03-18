package com.taskscheduler.tasksapi.domain.task;

import jakarta.persistence.Id;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@Entity
@Table(name = "task")
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue
    private UUID id;
    private UUID userId;
    private String title;
    private String description;
    private String priority;
    private String status;
}
