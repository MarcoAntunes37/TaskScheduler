package com.services.tasks;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import taskscheduller.api.TasksApplication;

@SpringBootTest(classes = TasksApplication.class)
@ActiveProfiles("test")
@Configuration()
public class TaskApplicationTest {
	@Bean
	@ServiceConnection
	public static PostgreSQLContainer<?> pgSqlContainer() {
		return new PostgreSQLContainer<>(DockerImageName.parse("postgres:17"));
	}

	@Test
	void contextLoads() {

	}

}
