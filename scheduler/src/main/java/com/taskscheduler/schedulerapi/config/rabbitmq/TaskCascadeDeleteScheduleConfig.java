package com.taskscheduler.schedulerapi.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskCascadeDeleteScheduleConfig {

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Bean
    Queue taskCascadeDeletionQueue() {
        return new Queue("task.cascade.delete.schedules.routing.key", false);
    }

    @Bean
    DirectExchange taskCascadeDeletionExchange() {
        return new DirectExchange("task.cascade.delete.schedules.exchange");
    }

    @Bean
    Binding bindTaskCascadeDeletionQueueExchange(Queue taskCascadeDeletionQueue,
            DirectExchange taskCascadeDeletionExchange) {
        return BindingBuilder
                .bind(taskCascadeDeletionQueue)
                .to(taskCascadeDeletionExchange)
                .with("task.cascade.delete.schedules.routing.key");
    }
}