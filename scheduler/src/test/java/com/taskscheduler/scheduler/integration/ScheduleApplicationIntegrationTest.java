package com.taskscheduler.scheduler.integration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;

import io.restassured.path.json.JsonPath;
import com.taskscheduler.schedulerapi.SchedulerApplication;
import com.taskscheduler.schedulerapi.domain.schedule.NewScheduleRequestDTO;
import com.taskscheduler.schedulerapi.domain.schedule.UpdateScheduleRequestDTO;

import io.restassured.RestAssured;

@SpringBootTest(classes = SchedulerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ScheduleApplicationIntegrationTest {
        @SuppressWarnings({ "rawtypes", "resource" })
        @ServiceConnection
        static PostgreSQLContainer<?> pgSqlContainer = new PostgreSQLContainer("postgres:17")
                        .withDatabaseName("testSchedulerDb")
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
        void shouldCreateSchedule() {
                NewScheduleRequestDTO dto = new NewScheduleRequestDTO(
                                UUID.randomUUID(),
                                UUID.randomUUID(),
                                OffsetDateTime.now().plusHours(1),
                                OffsetDateTime.now().plusDays(1));

                var responseBodyString = RestAssured.given()
                                .contentType("application/json")
                                .body(dto)
                                .when()
                                .post("/api/scheduler")
                                .then()
                                .log().all()
                                .statusCode(201)
                                .extract()
                                .body().asString();

                JsonPath jsonPath = new JsonPath(responseBodyString);

                assertThat(jsonPath.getUUID("id") != null);
                assertThat(jsonPath.getUUID("userId") == dto.userId());
                assertThat(jsonPath.get("startDate") == dto.startTime());
                assertThat(jsonPath.get("endDate") == dto.endTime());
        }

        @Test
        void shouldGetScheduleById() {
                NewScheduleRequestDTO dto = new NewScheduleRequestDTO(
                                UUID.randomUUID(),
                                UUID.randomUUID(),
                                OffsetDateTime.now().plusHours(1),
                                OffsetDateTime.now().plusDays(1));

                var responseBodyStringPost = RestAssured.given()
                                .contentType("application/json")
                                .body(dto)
                                .when()
                                .post("/api/scheduler")
                                .then()
                                .log().all()
                                .statusCode(201)
                                .extract()
                                .body().asString();

                JsonPath jsonPathPost = new JsonPath(responseBodyStringPost);

                var responseBodyStringGet = RestAssured.given()
                                .contentType("application/json")
                                .when()
                                .get("/api/scheduler/" + jsonPathPost.getUUID("id"))
                                .then()
                                .log().all()
                                .statusCode(200)
                                .extract()
                                .body().asString();

                JsonPath jsonPathGet = new JsonPath(responseBodyStringGet);

                assertThat(jsonPathGet.getUUID("id") == jsonPathPost.get("id"));
                assertThat(jsonPathGet.getUUID("userId") == dto.userId());
                assertThat(jsonPathGet.get("startDate") == dto.startTime());
                assertThat(jsonPathGet.get("endDate") == dto.endTime());
        }

        @Test
        void shouldGetAllSchedules() {
                int page = 0;
                int size = 10;
                String sortBy = "id";
                String sortDirection = "asc";
                OffsetDateTime startDate = OffsetDateTime.now();
                OffsetDateTime endDate = OffsetDateTime.now().plusYears(1);

                NewScheduleRequestDTO dto = new NewScheduleRequestDTO(
                                UUID.randomUUID(),
                                UUID.randomUUID(),
                                OffsetDateTime.now().plusHours(1),
                                OffsetDateTime.now().plusDays(1));

                var responseBodyStringPost = RestAssured.given()
                                .contentType("application/json")
                                .body(dto)
                                .when()
                                .post("/api/scheduler")
                                .then()
                                .log().all()
                                .statusCode(201)
                                .and()
                                .extract()
                                .body().asString();

                String url = "/api/scheduler/all/" + dto.userId() + "?page=" + page + "&size=" + size + "&sortBy="
                                + sortBy + "&sortDirection=" + sortDirection + "&startTime=" + startDate + "&endTime="
                                + endDate;

                var responseBodyStringGet = RestAssured.given()
                                .contentType("application/json")
                                .when()
                                .get(url)
                                .then()
                                .log().all()
                                .statusCode(200)
                                .extract()
                                .body().asString();

                JsonPath jsonPathGet = new JsonPath(responseBodyStringGet);
                JsonPath jsonPathPost = new JsonPath(responseBodyStringPost);

                assertThat(jsonPathGet.getList("content").size() == 1);

                assertThat(jsonPathGet.getUUID("content[0].id") == jsonPathPost.getUUID("id"));
                assertThat(jsonPathGet.getUUID("content[0].userId") == dto.userId());
                assertThat(jsonPathGet.get("content[0].startDate") == dto.startTime());
                assertThat(jsonPathGet.get("content[0].endDate") == dto.endTime());
        }

        @Test
        void shouldDeleteScheduleById() {
                NewScheduleRequestDTO dto = new NewScheduleRequestDTO(
                                UUID.randomUUID(),
                                UUID.randomUUID(),
                                OffsetDateTime.now().plusHours(1),
                                OffsetDateTime.now().plusDays(1));

                var responseBodyStringPost = RestAssured.given()
                                .contentType("application/json")
                                .body(dto)
                                .when()
                                .post("/api/scheduler")
                                .then()
                                .log().all()
                                .statusCode(201)
                                .extract()
                                .body().asString();

                JsonPath jsonPathPost = new JsonPath(responseBodyStringPost);

                var responseBodyStringDelete = RestAssured.given()
                                .contentType("application/json")
                                .when()
                                .delete("/api/scheduler/" + jsonPathPost.getUUID("id"))
                                .then()
                                .log().all()
                                .statusCode(200)
                                .extract()
                                .body().asString();

                assertThat(responseBodyStringDelete == "Schedule deleted successfully.");
        }

        @Test
        void shouldUpdateSchedule() {
                OffsetDateTime newStartTime = OffsetDateTime.now().plusHours(1);
                OffsetDateTime newEndTime = OffsetDateTime.now().plusHours(2);
                OffsetDateTime updateStartTime = OffsetDateTime.now().plusHours(1).plusDays(1);
                OffsetDateTime updateEndTime = OffsetDateTime.now().plusHours(2).plusDays(1);
                UUID taskId = UUID.randomUUID();
                NewScheduleRequestDTO dto = new NewScheduleRequestDTO(
                                taskId,
                                UUID.randomUUID(),
                                newStartTime,
                                newEndTime);

                var responseBodyStringPost = RestAssured.given()
                                .contentType("application/json")
                                .body(dto)
                                .when()
                                .post("/api/scheduler")
                                .then()
                                .log().all()
                                .statusCode(201)
                                .extract()
                                .body().asString();

                JsonPath jsonPathPost = new JsonPath(responseBodyStringPost);

                UpdateScheduleRequestDTO updateSchedule = new UpdateScheduleRequestDTO(
                                taskId,
                                updateStartTime,
                                updateEndTime);

                var responseBodyStringPut = RestAssured.given()
                                .contentType("application/json")
                                .body(updateSchedule)
                                .when()
                                .put("/api/scheduler/" + jsonPathPost.getUUID("id"))
                                .then()
                                .log().all()
                                .statusCode(200)
                                .extract()
                                .body().asString();

                JsonPath jsonPathPut = new JsonPath(responseBodyStringPut);

                assertThat(jsonPathPut.getUUID("id") == jsonPathPost.getUUID("id"));
                assertThat(jsonPathPut.getUUID("userId") == dto.userId());
                assertThat(jsonPathPut.get("startDate") == updateSchedule.startTime());
                assertThat(jsonPathPut.get("endDate") == updateSchedule.endTime());
        }
}