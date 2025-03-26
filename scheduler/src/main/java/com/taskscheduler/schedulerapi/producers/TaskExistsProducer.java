package com.taskscheduler.schedulerapi.producers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskscheduler.schedulerapi.domain.events.TaskExistsRequest;
import com.taskscheduler.schedulerapi.domain.events.TaskExistsResponse;

import groovy.util.logging.Slf4j;

@Service
@Slf4j
public class TaskExistsProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public boolean checkTaskExists(TaskExistsRequest request) {
        try {
            var response = rabbitTemplate
                    .convertSendAndReceive(
                            "task.exists.exchange",
                            "task.exists.routing.key",
                            request);

            if (response instanceof TaskExistsResponse) {
                TaskExistsResponse taskExistsResponse = (TaskExistsResponse) response;
                return taskExistsResponse.getExists();
            }

            if (response instanceof Boolean) {
                return (Boolean) response;
            }

            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error consulting task from database: {0}", e);
        }
    }
}
