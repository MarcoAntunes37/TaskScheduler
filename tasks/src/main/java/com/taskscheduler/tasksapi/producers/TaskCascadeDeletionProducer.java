package com.taskscheduler.tasksapi.producers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskscheduler.tasksapi.domain.events.TaskCascadeDeletionSchedulesRequest;

import groovy.util.logging.Slf4j;

@Service
@Slf4j
public class TaskCascadeDeletionProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public Integer deleteTaskSchedules(TaskCascadeDeletionSchedulesRequest request) {
        try {
            var rtResult = rabbitTemplate.convertSendAndReceive(
                    "task.cascade.delete.schedules.exchange",
                    "task.cascade.delete.schedules.routing.key",
                    request);

            if (rtResult instanceof Integer) {
                Integer response = (Integer) rtResult;
                return response;
            }

            return 0;
        } catch (Exception e) {
            throw new RuntimeException("Error deleting task from database: {0}", e);
        }
    }
}