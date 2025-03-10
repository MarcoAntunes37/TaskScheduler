package com.services.tasks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import taskScheduller.api.TasksApplication;

@SpringBootTest
@TestConfiguration(proxyBeanMethods = false)
class TaskApplicationTest {
	@Bean
	@ServiceConnection
	public static PostgreSQLContainer<?> mysqlContainer() {
		return new PostgreSQLContainer<>(DockerImageName.parse("postgres:17"));
	}

	public static void main(String[] args) {
		SpringApplication.from(TasksApplication::main)
				.with(TaskApplicationTest.class).run(args);
	}
}
