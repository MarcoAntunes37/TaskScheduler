package com.taskscheduler.schedulerapi.repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.taskscheduler.schedulerapi.domain.Schedule;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {

        @Query("FROM Schedule S WHERE S.userId = :userId " +
                        "AND (S.startTime BETWEEN :startTime AND :endTime " +
                        "OR S.endTime BETWEEN :startTime AND :endTime)")
        Page<Schedule> findAllByUserId(
                        UUID userId, OffsetDateTime startTime, OffsetDateTime endTime, Pageable pageable);

        List<Schedule> findAllByUserId(UUID userId);
}