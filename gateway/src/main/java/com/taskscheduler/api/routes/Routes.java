package com.taskscheduler.api.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;

@Configuration
public class Routes {

        @Value("${task.service.url}")
        private String taskServiceUrl;

        @Bean
        public RouterFunction<ServerResponse> taskServiceRouter() {
                return GatewayRouterFunctions.route("task_service")
                                .route(RequestPredicates.path("/api/tasks/**"), HandlerFunctions.http(taskServiceUrl))
                                .filter(CircuitBreakerFilterFunctions.circuitBreaker("taskServiceCircuitBreaker",
                                                URI.create("forward:/fallbackRoute")))
                                .build();
        }

        @Bean
        public RouterFunction<ServerResponse> taskServiceSwaggerRoute() {
                return GatewayRouterFunctions.route("task_service_swagger")
                                .route(RequestPredicates.path("/aggregate/task-service/v3/api-docs"),
                                                HandlerFunctions.http(taskServiceUrl))
                                .filter(CircuitBreakerFilterFunctions.circuitBreaker("taskServiceSwaggerCircuitBreaker",
                                                URI.create("forward:/fallbackRoute")))
                                .filter(setPath("/api-docs"))
                                .build();
        }

        @Bean
        public RouterFunction<ServerResponse> fallbackRoute() {
                return route("fallbackRoute")
                                .GET("/fallbackRoute", request -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
                                                .body("Service Unavailable, please try again later"))
                                .build();
        }
}