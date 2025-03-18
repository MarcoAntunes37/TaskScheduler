package com.taskscheduler.scheduler.unit.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.taskscheduler.schedulerapi.controller.ScheduleController;
import com.taskscheduler.schedulerapi.domain.NewScheduleRequestDTO;
import com.taskscheduler.schedulerapi.domain.Schedule;
import com.taskscheduler.schedulerapi.service.ScheduleService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScheduleController.class)
@AutoConfigureMockMvc
public class ScheduleControllerTest {
        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private ScheduleService scheduleService;

        @Test
        public void shouldCreateSchedule() throws Exception {
                ObjectMapper mapper = new ObjectMapper();

                mapper.registerModule(new JavaTimeModule());

                UUID userId = UUID.randomUUID();

                UUID taskId = UUID.randomUUID();

                OffsetDateTime startTime = OffsetDateTime.now();

                OffsetDateTime endTime = OffsetDateTime.now().plusDays(1);

                NewScheduleRequestDTO newScheduleRequestDTO = new NewScheduleRequestDTO(
                                userId, taskId, startTime, endTime);

                String json = mapper.writeValueAsString(newScheduleRequestDTO);

                Schedule schedule = new Schedule();

                when(scheduleService.createSchedule(newScheduleRequestDTO)).thenReturn(schedule);

                var result = mockMvc.perform(post("/api/tasks")
                                .content(json)
                                .contentType("application/json"))
                                .andDo(print())
                                .andReturn();

                assertThat(result.getResponse().getStatus() == 201);
        }

        @Test
        public void shoudlFailToCreateScheduleWithoutUserId() throws Exception {
                ObjectMapper mapper = new ObjectMapper();

                mapper.registerModule(new JavaTimeModule());

                NewScheduleRequestDTO newScheduleRequestDTO = new NewScheduleRequestDTO(
                                null, UUID.randomUUID(), OffsetDateTime.now(), OffsetDateTime.now().plusDays(1));

                String json = mapper.writeValueAsString(newScheduleRequestDTO);

                mockMvc.perform(post("/api/tasks")
                                .content(json)
                                .contentType("application/json"))
                                .andExpect(status().is4xxClientError())
                                .andDo(print());
        }

        @Test
        public void shoudlFailToCreateScheduleWithoutTaskId() throws Exception {
                ObjectMapper mapper = new ObjectMapper();

                mapper.registerModule(new JavaTimeModule());

                NewScheduleRequestDTO newScheduleRequestDTO = new NewScheduleRequestDTO(
                                UUID.randomUUID(), null, OffsetDateTime.now(), OffsetDateTime.now().plusDays(1));

                String json = mapper.writeValueAsString(newScheduleRequestDTO);

                mockMvc.perform(post("/api/tasks")
                                .content(json)
                                .contentType("application/json"))
                                .andExpect(status().is4xxClientError())
                                .andDo(print());
        }

        @Test
        public void shoudlFailToCreateScheduleWithoutStartTime() throws Exception {
                ObjectMapper mapper = new ObjectMapper();

                mapper.registerModule(new JavaTimeModule());

                NewScheduleRequestDTO newScheduleRequestDTO = new NewScheduleRequestDTO(
                                UUID.randomUUID(), UUID.randomUUID(), null, OffsetDateTime.now().plusDays(1));

                String json = mapper.writeValueAsString(newScheduleRequestDTO);

                mockMvc.perform(post("/api/tasks")
                                .content(json)
                                .contentType("application/json"))
                                .andExpect(status().is4xxClientError())
                                .andDo(print());
        }

        @Test
        public void shoudlFailToCreateScheduleWithoutEndTime() throws Exception {
                ObjectMapper mapper = new ObjectMapper();

                mapper.registerModule(new JavaTimeModule());

                NewScheduleRequestDTO newScheduleRequestDTO = new NewScheduleRequestDTO(
                                UUID.randomUUID(), UUID.randomUUID(), OffsetDateTime.now(), null);

                String json = mapper.writeValueAsString(newScheduleRequestDTO);

                mockMvc.perform(post("/api/tasks")
                                .content(json)
                                .contentType("application/json"))
                                .andExpect(status().is4xxClientError())
                                .andDo(print());
        }

        @Test
        public void shoudlFailToCreateScheduleWithStartTimeIsInThePast() throws Exception {
                ObjectMapper mapper = new ObjectMapper();

                mapper.registerModule(new JavaTimeModule());

                NewScheduleRequestDTO newScheduleRequestDTO = new NewScheduleRequestDTO(
                                UUID.randomUUID(), UUID.randomUUID(), OffsetDateTime.now().minusDays(1),
                                OffsetDateTime.now());

                String json = mapper.writeValueAsString(newScheduleRequestDTO);

                mockMvc.perform(post("/api/tasks")
                                .content(json)
                                .contentType("application/json"))
                                .andExpect(status().is4xxClientError())
                                .andDo(print());
        }

        @Test
        public void shoudlFailToCreateScheduleWithEndTimeIsInThePast() throws Exception {
                ObjectMapper mapper = new ObjectMapper();

                mapper.registerModule(new JavaTimeModule());

                NewScheduleRequestDTO newScheduleRequestDTO = new NewScheduleRequestDTO(
                                UUID.randomUUID(), UUID.randomUUID(), OffsetDateTime.now(),
                                OffsetDateTime.now().minusDays(1));

                String json = mapper.writeValueAsString(newScheduleRequestDTO);

                mockMvc.perform(post("/api/tasks")
                                .content(json)
                                .contentType("application/json"))
                                .andExpect(status().is4xxClientError())
                                .andDo(print());
        }

        @Test
        public void shoudlFailToCreateScheduleWhenSheduleStartsAfterEnd() throws Exception {
                ObjectMapper mapper = new ObjectMapper();

                mapper.registerModule(new JavaTimeModule());

                NewScheduleRequestDTO newScheduleRequestDTO = new NewScheduleRequestDTO(
                                UUID.randomUUID(), UUID.randomUUID(), OffsetDateTime.now().plusDays(1),
                                OffsetDateTime.now());

                String json = mapper.writeValueAsString(newScheduleRequestDTO);

                mockMvc.perform(post("/api/tasks")
                                .content(json)
                                .contentType("application/json"))
                                .andExpect(status().is4xxClientError())
                                .andDo(print());
        }

        @Test
        public void shoudlFailToCreateScheduleWithStartTimeAndEndTimeIsSame() throws Exception {
                ObjectMapper mapper = new ObjectMapper();

                mapper.registerModule(new JavaTimeModule());

                NewScheduleRequestDTO newScheduleRequestDTO = new NewScheduleRequestDTO(
                                UUID.randomUUID(), UUID.randomUUID(), OffsetDateTime.now(), OffsetDateTime.now());

                String json = mapper.writeValueAsString(newScheduleRequestDTO);

                mockMvc.perform(post("/api/tasks")
                                .content(json)
                                .contentType("application/json"))
                                .andExpect(status().is4xxClientError())
                                .andDo(print());
        }

        @Test
        public void shoudlFailToCreateScheduleWhenScheduleOverlapAnother() throws Exception {
                ObjectMapper mapper = new ObjectMapper();

                mapper.registerModule(new JavaTimeModule());

                NewScheduleRequestDTO newScheduleRequestDTO = new NewScheduleRequestDTO(
                                UUID.randomUUID(), UUID.randomUUID(), OffsetDateTime.now(),
                                OffsetDateTime.now().plusDays(1));

                String json = mapper.writeValueAsString(newScheduleRequestDTO);

                mockMvc.perform(post("/api/tasks")
                                .content(json)
                                .contentType("application/json"))
                                .andDo(print());

                mockMvc.perform(post("/api/tasks")
                                .content(json)
                                .contentType("application/json"))
                                .andExpect(status().is4xxClientError())
                                .andDo(print());
        }

        @Test
        public void shouldGetScheduleById() throws Exception {
                UUID scheduleId = UUID.randomUUID();

                Schedule responseTask = Schedule.builder()
                                .id(scheduleId)
                                .userId(UUID.randomUUID())
                                .taskId(UUID.randomUUID())
                                .startTime(OffsetDateTime.now())
                                .endTime(OffsetDateTime.now().plusDays(1))
                                .build();

                ObjectMapper mapper = new ObjectMapper();

                mapper.registerModule(new JavaTimeModule());

                when(scheduleService.getScheduleById(scheduleId)).thenReturn(responseTask);

                var result = this.mockMvc.perform(get("/api/tasks/{scheduleId}", scheduleId))
                                .andReturn();

                assertThat(result.getResponse().getStatus() == 200);

                String json = mapper.writeValueAsString(result.getResponse().getContentAsString());

                assertThat(json.contains(scheduleId.toString()));
        }

}
