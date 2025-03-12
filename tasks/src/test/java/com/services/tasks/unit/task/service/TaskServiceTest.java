package com.services.tasks.unit.task.service;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import taskScheduller.api.domain.task.NewTaskRequestDto;
import taskScheduller.api.domain.task.Task;
import taskScheduller.api.domain.task.UpdateTaskRequestDto;
import taskScheduller.api.repository.TaskRepository;
import taskScheduller.api.service.TaskService;

@ActiveProfiles("test")
class TaskServiceTest {
    @Mock
    private TaskRepository repository;

    @InjectMocks
    private TaskService taskServices;

    UUID userId = UUID.randomUUID();

    Task testTask1 = Task.builder()
            .id(UUID.randomUUID())
            .userId(userId)
            .title("title")
            .description("description")
            .priority("priority")
            .status("status")
            .build();

    Task testTask2 = Task.builder()
            .id(UUID.randomUUID())
            .userId(userId)
            .title("title")
            .description("description")
            .priority("priority")
            .status("status")
            .build();

    NewTaskRequestDto newTask = new NewTaskRequestDto(
            UUID.randomUUID(),
            "title",
            "description",
            "priority",
            "status");

    UpdateTaskRequestDto updateTask = new UpdateTaskRequestDto(
            "atitle",
            "description",
            "priority",
            "status");

    Task task = new Task();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        taskServices = new TaskService(repository);

    }

    @Test
    public void shouldAddTask() {
        when(repository.save(any(Task.class))).thenReturn(task);

        taskServices.saveTask(newTask);

        verify(repository, times(1)).save(any(Task.class));
    }

    @Test
    public void shouldGetTaskById() {
        UUID taskId = testTask1.getId();
        when(repository.findById(taskId)).thenReturn(Optional.of(testTask1));
        taskServices.getTaskById(taskId);
        verify(repository).findById(taskId);
    }

    @Test
    public void shouldNotGetTaskById() {
        UUID taskId = testTask1.getId();
        when(repository.findById(taskId)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> taskServices.getTaskById(taskId));
        verify(repository).findById(taskId);
    }

    @Test
    public void shouldDeleteTask() {
        UUID taskId = UUID.randomUUID();
        when(repository.existsById(taskId)).thenReturn(true);
        taskServices.deleteTask(taskId);
        verify(repository, times(1)).deleteById(taskId);
    }

    @Test
    public void shouldNotDeleteTask() {
        UUID taskId = UUID.randomUUID();
        when(repository.existsById(taskId)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> taskServices.deleteTask(taskId));
        verify(repository, never()).deleteById(taskId);
    }

    @Test
    public void shouldUpdateTask() {
        when(repository.findById(testTask1.getId())).thenReturn(Optional.of(testTask1));
        taskServices.updateTask(testTask1.getId(), updateTask);
        verify(repository, times(1)).save(any(Task.class));
    }

    @Test
    public void shouldNotUpdateTask() {
        when(repository.findById(testTask1.getId())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> taskServices.updateTask(testTask1.getId(), updateTask));
        verify(repository, never()).save(any(Task.class));
    }

    @Test
    public void shouldReturnAllTasks() {

        Page<Task> page = new PageImpl<>(List.of(testTask1, testTask2));

        when(taskServices.getAllTasks(userId, 0, 10, "id", "asc")).thenReturn(page);

        taskServices.getAllTasks(userId, 0, 10, "id", "asc");

        verify(repository).findAllByUserId(userId, PageRequest.of(0, 10, Sort.by("id").ascending()));
    }

    @Test
    public void shouldReturnEmptyAllTasks() {
        UUID userId = UUID.randomUUID();

        Page<Task> page = new PageImpl<>(List.of());

        when(taskServices.getAllTasks(userId, 0, 10, "id", "asc")).thenReturn(page);

        taskServices.getAllTasks(userId, 0, 10, "id", "asc");

        verify(repository).findAllByUserId(userId, PageRequest.of(0, 10, Sort.by("id").ascending()));
    }

    @Test
    public void shouldReturnAllTasksFiltered() {
        Page<Task> page = new PageImpl<>(List.of(testTask2));

        when(taskServices.getAllTasksFiltered(userId, 0, 10, "a", "id", "asc")).thenReturn(page);

        taskServices.getAllTasksFiltered(userId, 0, 10, "a", "id", "asc");

        verify(repository).findAllByUserIdFiltered(userId, PageRequest.of(0, 10, Sort.by("id").ascending()), "a");
    }

    @Test
    public void shouldReturnEmptyAllTasksFiltered() {
        UUID userId = UUID.randomUUID();

        Page<Task> page = new PageImpl<>(List.of());

        when(taskServices.getAllTasksFiltered(userId, 0, 10, "a", "id", "asc")).thenReturn(page);

        taskServices.getAllTasksFiltered(userId, 0, 10, "a", "id", "asc");

        verify(repository).findAllByUserIdFiltered(userId, PageRequest.of(0, 10, Sort.by("id").ascending()), "a");
    }

}