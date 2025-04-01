package com.taskscheduler.tasksapi.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakEventsConfig {

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Bean
    Queue keycloakEventsQueue() {
        return new Queue("keycloak.events.account-delete", false);
    }

    @Bean
    DirectExchange keycloakEventsExchange() {
        return new DirectExchange("keycloak.events.exchange");
    }

    @Bean
    Binding bindKeycloakEventsQueueExchange(
            Queue keycloakEventsQueue,
            DirectExchange keycloakEventsExchange) {
        return BindingBuilder
                .bind(keycloakEventsQueue)
                .to(keycloakEventsExchange)
                .with("keycloak.events.account-delete");
    }
}
