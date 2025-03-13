package com.taskscheduler.api.integration.task;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import taskscheduler.api.TasksApplication;
import taskscheduler.api.domain.task.NewTaskRequestDto;
import taskscheduler.api.domain.task.UpdateTaskRequestDto;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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

        @Test
        void shouldCreateTask() {
                NewTaskRequestDto dto = new NewTaskRequestDto(
                                UUID.randomUUID(),
                                "title",
                                "description",
                                "priority",
                                "status");

                var responseBodyString = RestAssured.given()
                                .contentType("application/json")
                                .body(dto)
                                .when()
                                .post("/api/tasks")
                                .then()
                                .log().all()
                                .statusCode(201)
                                .extract()
                                .body().asString();

                JsonPath jsonPath = new JsonPath(responseBodyString);

                assertThat(jsonPath.getUUID("id") != null);
                assertThat(jsonPath.getUUID("userId") == dto.userId());
                assertThat(jsonPath.getString("title") == dto.title());
                assertThat(jsonPath.getString("description") == dto.description());
                assertThat(jsonPath.getString("priority") == dto.priority());
                assertThat(jsonPath.getString("status") == dto.status());
        }

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

        @Test
        void shouldGetTaskById() throws JSONException {
                NewTaskRequestDto dto = new NewTaskRequestDto(
                                UUID.randomUUID(),
                                "title",
                                "description",
                                "priority",
                                "status");

                var responseBodyStringPost = RestAssured.given()
                                .contentType("application/json")
                                .body(dto)
                                .when()
                                .post("/api/tasks")
                                .then()
                                .log().all()
                                .statusCode(201)
                                .extract()
                                .body().asString();

                JSONObject jsonResponsePost = new JSONObject(responseBodyStringPost);

                var responseBodyStringGet = RestAssured.given()
                                .contentType("application/json")
                                .when()
                                .get("/api/tasks/" + jsonResponsePost.getString("id"))
                                .then()
                                .log().all()
                                .extract()
                                .body().asString();

                JSONObject jsonResponseGet = new JSONObject(responseBodyStringGet);

                assertThat(jsonResponseGet.getString("id") == jsonResponsePost.getString("id"));
        }

        @Test
        void shouldGetAllTasks() throws JSONException {
                NewTaskRequestDto dto = new NewTaskRequestDto(
                                UUID.randomUUID(),
                                "title",
                                "description",
                                "priority",
                                "status");

                var responseBodyStringPost = RestAssured.given()
                                .contentType("application/json")
                                .body(dto)
                                .when()
                                .post("/api/tasks")
                                .then()
                                .log().all()
                                .statusCode(201)
                                .extract()
                                .body().asString();

                JSONObject jsonResponsePost = new JSONObject(responseBodyStringPost);

                String userId = jsonResponsePost.getString("userId");
                int page = 0;
                int size = 10;
                String sortBy = "id";
                String sortDirection = "asc";
                String url = "/api/tasks/all/" + userId + "?page=" + page + "&size=" + size + "&sortBy=" + sortBy
                                + "&sortDirection=" + sortDirection;
                var responseBodyStringGet = RestAssured.given()
                                .contentType("application/json")
                                .when()
                                .get(url)
                                .then()
                                .log().all()
                                .extract()
                                .body().asString();

                JSONObject jsonResponseGet = new JSONObject(responseBodyStringGet);

                String taskId = jsonResponseGet
                                .optJSONArray("content")
                                .getJSONObject(0)
                                .getString("id");

                assertThat(taskId == jsonResponsePost.getString("id"));
        }

        @Test
        void shouldGetAllTasksFiltered() throws JSONException {
                NewTaskRequestDto dto = new NewTaskRequestDto(
                                UUID.randomUUID(),
                                "title",
                                "description",
                                "priority",
                                "status");

                NewTaskRequestDto dto1 = new NewTaskRequestDto(
                                UUID.randomUUID(),
                                "atitle",
                                "adescription",
                                "apriority",
                                "astatus");

                var responseBodyStringPost = RestAssured.given()
                                .contentType("application/json")
                                .body(dto)
                                .when()
                                .post("/api/tasks")
                                .then()
                                .log().all()
                                .statusCode(201)
                                .extract()
                                .body().asString();

                var responseBodyStringPost1 = RestAssured.given()
                                .contentType("application/json")
                                .body(dto1)
                                .when()
                                .post("/api/tasks")
                                .then()
                                .log().all()
                                .statusCode(201)
                                .extract()
                                .body().asString();

                JSONObject jsonResponsePost = new JSONObject(responseBodyStringPost);
                JSONObject jsonResponsePost1 = new JSONObject(responseBodyStringPost1);

                String userId = jsonResponsePost.getString("userId");
                int page = 0;
                int size = 10;
                String searchTerm = "a";
                String sortBy = "id";
                String sortDirection = "asc";
                String url = "/api/tasks/all/" + userId + "?page=" + page + "&size=" + size + "&searchTerm="
                                + searchTerm
                                + "&sortBy=" + sortBy
                                + "&sortDirection=" + sortDirection;

                var responseBodyStringGet = RestAssured.given()
                                .contentType("application/json")
                                .when()
                                .get(url)
                                .then()
                                .log().all()
                                .extract()
                                .body().asString();

                JSONObject jsonResponseGet = new JSONObject(responseBodyStringGet);

                String taskId = jsonResponseGet
                                .optJSONArray("content")
                                .getJSONObject(0)
                                .getString("id");

                String title = jsonResponseGet
                                .optJSONArray("content")
                                .getJSONObject(0)
                                .getString("title");

                assertThat(taskId == jsonResponsePost1.getString("id"));
                assertThat(title == jsonResponsePost1.getString("title"));
        }

        @Test
        void shouldUpdateTask() throws JSONException {
                NewTaskRequestDto dto = new NewTaskRequestDto(
                                UUID.randomUUID(),
                                "title",
                                "description",
                                "priority",
                                "status");

                var responseBodyStringPost = RestAssured.given()
                                .contentType("application/json")
                                .body(dto)
                                .when()
                                .post("/api/tasks")
                                .then()
                                .log().all()
                                .statusCode(201)
                                .extract()
                                .body().asString();

                JSONObject jsonResponsePost = new JSONObject(responseBodyStringPost);

                UpdateTaskRequestDto dto1 = new UpdateTaskRequestDto("atitle",
                                "adescription",
                                "apriority",
                                "astatus");

                var responseBodyStringPut = RestAssured.given()
                                .contentType("application/json")
                                .body(dto1)
                                .when()
                                .put("/api/tasks/" + jsonResponsePost.getString("id"))
                                .then()
                                .log().all()
                                .statusCode(200)
                                .extract()
                                .body().asString();

                assertThat(responseBodyStringPut == "Task updated successfully");
        }

        @Test
        void shouldDeleteTask() throws JSONException {
                NewTaskRequestDto dto = new NewTaskRequestDto(
                                UUID.randomUUID(),
                                "title",
                                "description",
                                "priority",
                                "status");

                var responseBodyStringPost = RestAssured.given()
                                .contentType("application/json")
                                .body(dto)
                                .when()
                                .post("/api/tasks")
                                .then()
                                .log().all()
                                .statusCode(201)
                                .extract()
                                .body().asString();

                JSONObject jsonResponsePost = new JSONObject(responseBodyStringPost);

                var responseBodyStringPut = RestAssured.given()
                                .contentType("application/json")
                                .when()
                                .delete("/api/tasks/" + jsonResponsePost.getString("id"))
                                .then()
                                .log().all()
                                .statusCode(200)
                                .extract()
                                .body().asString();

                assertThat(responseBodyStringPut == "Task deleted successfully");
        }
}