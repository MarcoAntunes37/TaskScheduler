// package com.services.tasks.unit.task.controller;

// import static org.mockito.Mockito.when;

// import java.util.UUID;

// import org.junit.Before;
// import org.junit.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;

// import com.fasterxml.jackson.databind.ObjectMapper;

// import taskScheduller.api.controller.TaskController;
// import taskScheduller.api.domain.task.NewTaskRequestDto;
// import taskScheduller.api.domain.task.Task;
// import taskScheduller.api.service.TaskService;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
// import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

// @WebMvcTest(TaskController.class)
// public class TaskControllerTest {
//     @Autowired
//     private MockMvc mockMvc;

//     @MockBean
//     private TaskService taskServices;

//     @Autowired
//     private ObjectMapper objectMapper;

//     @Before
//     public void setUp() {
//         taskServices = new TaskService(taskServices);
//     }

//     // @Test
//     // public void shouldCreateTask() throws Exception {
//     // NewTaskRequestDto task = new NewTaskRequestDto(
//     // UUID.randomUUID(),
//     // "title",
//     // "description",
//     // "priority",
//     // "status");

//     // Task responseTask = new Task();

//     // when(taskServices.saveTask(task)).thenReturn(responseTask);

//     // mockMvc.perform(post("/api/tasks")
//     // .contentType(MediaType.APPLICATION_JSON)
//     // .content("teste"))
//     // .andDo(print())
//     // .andExpect(status().isOk());
//     // }

//     @Test
//     public void shouldGetTaskById() throws Exception {
//         UUID taskId = UUID.randomUUID();
//         Task taskResult = new Task();

//         mockMvc.perform(get("/api/tasks/{taskId}", taskId))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.title").exists());
//     }
// }