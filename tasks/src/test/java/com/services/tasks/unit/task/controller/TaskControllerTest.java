package com.services.tasks.unit.task.controller;

import java.util.List;
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import taskScheduller.api.controller.TaskController;
import taskScheduller.api.domain.task.NewTaskRequestDto;
import taskScheduller.api.domain.task.Task;
import taskScheduller.api.service.TaskService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TaskController.class)
@AutoConfigureMockMvc
public class TaskControllerTest {
        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private TaskService taskServices;

        @Test
        public void shouldCreateTask() throws Exception {
                ObjectMapper mapper = new ObjectMapper();
                NewTaskRequestDto taskdto = new NewTaskRequestDto(
                                UUID.randomUUID(),
                                "test",
                                "test",
                                "test",
                                "test");

                String json = mapper.writeValueAsString(taskdto);
                Task responseTask = new Task();

                when(taskServices.saveTask(taskdto)).thenReturn(responseTask);

                var result = mockMvc.perform(post("/api/tasks")
                                .content(json)
                                .contentType("application/json"))
                                .andDo(print())
                                .andReturn();

                assertThat(result.getResponse().getStatus() == 201);
        }

        @Test
        public void shouldFailToCreateTaskWithoutUserId() throws Exception {
                ObjectMapper mapper = new ObjectMapper();
                NewTaskRequestDto taskdto = new NewTaskRequestDto(
                                null,
                                "test",
                                "test",
                                "test",
                                "test");

                String json = mapper.writeValueAsString(taskdto);

                mockMvc.perform(post("/api/tasks")
                                .content(json)
                                .contentType("application/json"))
                                .andExpect(status().is4xxClientError())
                                .andDo(print());
        }

        @Test
        public void shouldFailToCreateTaskWithoutTitle() throws Exception {
                ObjectMapper mapper = new ObjectMapper();
                NewTaskRequestDto taskdto = new NewTaskRequestDto(
                                UUID.randomUUID(),
                                null,
                                "test",
                                "test",
                                "test");

                String json = mapper.writeValueAsString(taskdto);

                mockMvc.perform(post("/api/tasks")
                                .content(json)
                                .contentType("application/json"))
                                .andExpect(status().is4xxClientError())
                                .andDo(print());
        }

        @Test
        public void shouldFailToCreateTaskWithoutDescription() throws Exception {
                ObjectMapper mapper = new ObjectMapper();
                NewTaskRequestDto taskdto = new NewTaskRequestDto(
                                UUID.randomUUID(),
                                "test",
                                null,
                                "test",
                                "test");

                String json = mapper.writeValueAsString(taskdto);

                mockMvc.perform(post("/api/tasks")
                                .content(json)
                                .contentType("application/json"))
                                .andExpect(status().is4xxClientError())
                                .andDo(print());
        }

        @Test
        public void shouldFailToCreateTaskWithoutPriority() throws Exception {
                ObjectMapper mapper = new ObjectMapper();
                NewTaskRequestDto taskdto = new NewTaskRequestDto(
                                UUID.randomUUID(),
                                "test",
                                "test",
                                null,
                                "test");

                String json = mapper.writeValueAsString(taskdto);

                mockMvc.perform(post("/api/tasks")
                                .content(json)
                                .contentType("application/json"))
                                .andExpect(status().is4xxClientError())
                                .andDo(print());
        }

        @Test
        public void shouldFailToCreateTaskWithoutStatus() throws Exception {
                ObjectMapper mapper = new ObjectMapper();
                NewTaskRequestDto taskdto = new NewTaskRequestDto(
                                UUID.randomUUID(),
                                "test",
                                "test",
                                "test",
                                null);

                String json = mapper.writeValueAsString(taskdto);

                mockMvc.perform(post("/api/tasks")
                                .content(json)
                                .contentType("application/json"))
                                .andExpect(status().is4xxClientError())
                                .andDo(print());
        }

        @Test
        public void shouldGetTaskById() throws Exception {
                UUID taskId = UUID.randomUUID();
                Task responseTask = new Task();
                ObjectMapper mapper = new ObjectMapper();
                when(taskServices.getTaskById(taskId)).thenReturn(responseTask);

                var result = this.mockMvc.perform(get("/api/tasks/{taskId}", taskId))
                                .andReturn();

                assertThat(result.getResponse().getStatus() == 200);
                String json = mapper.writeValueAsString(result.getResponse().getContentAsString());
                assertThat(json.contains(taskId.toString()));
        }

        @Test
        public void shouldGetAllTasks() throws Exception {
                String userId = UUID.randomUUID().toString();
                int page = 0;
                int size = 10;
                String sortBy = "id";
                String sortDirection = "asc";
                Page<Task> responseTasks = new PageImpl<>(List.of(new Task()));

                when(taskServices.getAllTasks(UUID.fromString(userId), page, size, sortBy, sortDirection))
                                .thenReturn(responseTasks);

                var response = this.mockMvc
                                .perform(get("/api/tasks/all/{userId}?page={page}&size={size}&sortBy={sortBy}&sortDirection={sortDirection}",
                                                userId, page, size, sortBy, sortDirection))
                                .andReturn();

                assertThat(response.getResponse().getStatus() == 200);
                verify(taskServices, times(1)).getAllTasks(UUID.fromString(userId), page, size, sortBy, sortDirection);
        }

        @Test
        public void shouldGetAllTasksFiltered() throws Exception {
                String userId = UUID.randomUUID().toString();
                int page = 0;
                int size = 10;
                String searchTerm = "test";
                String sortBy = "id";
                String sortDirection = "asc";
                Page<Task> responseTasks = new PageImpl<>(List.of(new Task()));

                when(taskServices.getAllTasksFiltered(UUID.fromString(userId), page, size, searchTerm, sortBy,
                                sortDirection))
                                .thenReturn(responseTasks);

                var response = this.mockMvc
                                .perform(get(
                                                "/api/tasks/all/{userId}/filtered?page={page}&size={size}&searchTerm={searchTerm}&sortBy={sortBy}&sortDirection={sortDirection}",
                                                userId, page, size, searchTerm, sortBy, sortDirection))
                                .andReturn();

                assertThat(response.getResponse().getStatus() == 200);
                verify(taskServices, times(1)).getAllTasksFiltered(UUID.fromString(userId), page, size, searchTerm,
                                sortBy,
                                sortDirection);
        }
}