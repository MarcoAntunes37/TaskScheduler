package com.taskscheduler.tasksapi.consumers;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class KeycloakEventsConsumer {

    @RabbitListener(queues = "keycloak.events.routing.key")
    public void consumeKeycloakEvents() throws Exception {
        System.err.println("Keycloak events received");
    }
}
