package com.taskscheduler.schedulerapi.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskExistsConfig {

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Bean
    Queue taskExistsQueue() {
        return new Queue("task.exists.routing.key", false);
    }

    @Bean
    DirectExchange taskExistsExchange() {
        return new DirectExchange("task.exists.exchange");
    }

    @Bean
    Binding bindTaskExistsQueueExchange(Queue taskExistsQueue, DirectExchange taskExistsExchange) {
        return BindingBuilder
                .bind(taskExistsQueue)
                .to(taskExistsExchange)
                .with("task.exists.routing.key");
    }
}
