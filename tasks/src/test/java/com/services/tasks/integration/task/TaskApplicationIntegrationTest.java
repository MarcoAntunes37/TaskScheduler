package com.services.tasks.integration.task;

import io.restassured.RestAssured;
import taskScheduller.api.TasksApplication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest(classes = TasksApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskApplicationIntegrationTest {

    @SuppressWarnings({ "rawtypes", "resource" })
    @ServiceConnection
    static PostgreSQLContainer<?> pgSqlContainer = new PostgreSQLContainer("postgres:17")
            .withDatabaseName("testTasksDb")
            .withUsername("postgres")
            .withPassword("postgres");
    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    static {
        pgSqlContainer.start();
    }

    // @Test
    // void shouldAddTask() {
    // NewTaskRequestDto task = new NewTaskRequestDto(
    // UUID.randomUUID(),
    // "title",
    // "description",
    // "priority",
    // "status"
    // );

    // var responseBodyString = RestAssured.given()
    // .contentType("application/json")
    // .body(task)
    // .when()
    // .post("/api/tasks")
    // .then()
    // .log().all()
    // .statusCode(201)
    // .extract()
    // .body().asString();

    // JsonPath jsonPath = new JsonPath(responseBodyString);

    // assertThat(jsonPath.getString("id"), Matchers.notNullValue());
    // assertThat(jsonPath.getString("userId"),
    // Matchers.is(task.userId().toString()));
    // assertThat(jsonPath.getString("title"), Matchers.is(task.title()));
    // assertThat(jsonPath.getString("description"),
    // Matchers.is(task.description()));
    // assertThat(jsonPath.getString("priority"), Matchers.is(task.priority()));
    // assertThat(jsonPath.getString("status"), Matchers.is(task.status()));
    // }

    @Test
    void shouldFailCreateTask() {
        RestAssured.given()
                .contentType("application/json")
                .body("")
                .when()
                .post("/api/tasks")
                .then()
                .log().all()
                .statusCode(400);
    }
}