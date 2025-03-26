package com.taskscheduler.schedulerapi.consumers;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taskscheduler.schedulerapi.domain.events.TaskCascadeDeletionSchedulesRequest;
import com.taskscheduler.schedulerapi.service.ScheduleService;

@Component
public class TaskCascadeDeleteSchedulesConsumer {
    @Autowired
    final ScheduleService scheduleService;

    public TaskCascadeDeleteSchedulesConsumer(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @RabbitListener(queues = "task.cascade.delete.schedules.routing.key")
    public Integer deleteTaskSchedules(TaskCascadeDeletionSchedulesRequest request) {
        Integer response = scheduleService.deleteAllSchedulesByTaskId(request.getTaskId());

        return response;
    }
}