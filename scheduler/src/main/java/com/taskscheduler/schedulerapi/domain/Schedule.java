package com.taskscheduler.schedulerapi.domain;

import jakarta.persistence.Id;

import java.time.OffsetDateTime;
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
@Table(name = "schedules")
@AllArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue
    private UUID id;
    private UUID taskId;
    private UUID userId;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
}