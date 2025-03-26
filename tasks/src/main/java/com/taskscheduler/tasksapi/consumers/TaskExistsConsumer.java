package com.taskscheduler.tasksapi.consumers;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.taskscheduler.tasksapi.domain.events.TaskExistsRequest;
import com.taskscheduler.tasksapi.service.TaskService;

@Component
public class TaskExistsConsumer {
    final TaskService taskService;

    public TaskExistsConsumer(TaskService taskService) {
        this.taskService = taskService;
    }

    @RabbitListener(queues = "task.exists.routing.key")
    public boolean checkTask(TaskExistsRequest request) throws Exception {
        return taskService.taskExists(request.getTaskId());
    }
}
