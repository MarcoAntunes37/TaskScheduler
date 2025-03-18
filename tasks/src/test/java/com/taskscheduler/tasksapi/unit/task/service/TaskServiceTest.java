package com.taskscheduler.tasksapi.unit.task.service;

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

import com.taskscheduler.tasksapi.domain.task.NewTaskRequestDto;
import com.taskscheduler.tasksapi.domain.task.Task;
import com.taskscheduler.tasksapi.domain.task.UpdateTaskRequestDto;
import com.taskscheduler.tasksapi.repository.TaskRepository;
import com.taskscheduler.tasksapi.service.TaskService;

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
    public void shouldCreateTask() {
        when(repository.save(any(Task.class))).thenReturn(task);

        taskServices.saveTask(newTask);

        verify(repository, times(1)).save(any(Task.class));
    }

    @Test
    public void shouldNotCreateTaskWhenUserIdIsNull() {
        NewTaskRequestDto newTask = new NewTaskRequestDto(null, "title", "description", "priority", "status");
        when(repository.save(any(Task.class))).thenThrow(new RuntimeException("UserId is required"));
        assertThrows(RuntimeException.class, () -> taskServices.saveTask(newTask));
        verify(repository, times(1)).save(any(Task.class));
    }

    @Test
    public void shouldNotCreateTaskWhenTitleIsNull() {
        NewTaskRequestDto newTask = new NewTaskRequestDto(UUID.randomUUID(), null, "description", "priority", "status");
        when(repository.save(any(Task.class))).thenThrow(new RuntimeException("Title is required"));
        assertThrows(RuntimeException.class, () -> taskServices.saveTask(newTask));
        verify(repository, times(1)).save(any(Task.class));
    }

    @Test
    public void shouldNotCreateTaskWhenTitleIsEmpty() {
        NewTaskRequestDto newTask = new NewTaskRequestDto(UUID.randomUUID(), "", "description", "priority", "status");
        when(repository.save(any(Task.class))).thenThrow(new RuntimeException("Title is required"));
        assertThrows(RuntimeException.class, () -> taskServices.saveTask(newTask));
        verify(repository, times(1)).save(any(Task.class));
    }

    @Test
    public void shouldNotCreateTaskWhenDescriptionIsNull() {
        NewTaskRequestDto newTask = new NewTaskRequestDto(UUID.randomUUID(), "title", null, "priority", "status");
        when(repository.save(any(Task.class))).thenThrow(new RuntimeException("Description is required"));
        assertThrows(RuntimeException.class, () -> taskServices.saveTask(newTask));
        verify(repository, times(1)).save(any(Task.class));
    }

    @Test
    public void shouldNotCreateTaskWhenDescriptionIsEmpty() {
        NewTaskRequestDto newTask = new NewTaskRequestDto(UUID.randomUUID(), "title", "", "priority", "status");
        when(repository.save(any(Task.class))).thenThrow(new RuntimeException("Description is required"));
        assertThrows(RuntimeException.class, () -> taskServices.saveTask(newTask));
        verify(repository, times(1)).save(any(Task.class));
    }

    @Test
    public void shouldNotCreateTaskWhenPriorityIsNull() {
        NewTaskRequestDto newTask = new NewTaskRequestDto(UUID.randomUUID(), "title", "description", null, "status");
        when(repository.save(any(Task.class))).thenThrow(new RuntimeException("Priority is required"));
        assertThrows(RuntimeException.class, () -> taskServices.saveTask(newTask));
        verify(repository, times(1)).save(any(Task.class));
    }

    @Test
    public void shouldNotCreateTaskWhenPriorityIsEmpty() {
        NewTaskRequestDto newTask = new NewTaskRequestDto(UUID.randomUUID(), "title", "description", "", "status");
        when(repository.save(any(Task.class))).thenThrow(new RuntimeException("Priority is required"));
        assertThrows(RuntimeException.class, () -> taskServices.saveTask(newTask));
        verify(repository, times(1)).save(any(Task.class));
    }

    @Test
    public void shouldNotCreateTaskWhenStatusIsNull() {
        NewTaskRequestDto newTask = new NewTaskRequestDto(UUID.randomUUID(), "title", "description", "priority", null);
        when(repository.save(any(Task.class))).thenThrow(new RuntimeException("Status is required"));
        assertThrows(RuntimeException.class, () -> taskServices.saveTask(newTask));
        verify(repository, times(1)).save(any(Task.class));
    }

    @Test
    public void shouldNotCreateTaskWhenStatusIsEmpty() {
        NewTaskRequestDto newTask = new NewTaskRequestDto(UUID.randomUUID(), "title", "description", "priority", "");
        when(repository.save(any(Task.class))).thenThrow(new RuntimeException("Status is required"));
        assertThrows(RuntimeException.class, () -> taskServices.saveTask(newTask));
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
        when(repository.findById(testTask1.getId())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> taskServices.getTaskById(testTask1.getId()));
        verify(repository).findById(testTask1.getId());
    }

    @Test
    public void shouldDeleteTask() {
        when(repository.existsById(testTask1.getId())).thenReturn(true);
        taskServices.deleteTask(testTask1.getId());
        verify(repository, times(1)).deleteById(testTask1.getId());
    }

    @Test
    public void shouldNotDeleteTask() {
        when(repository.existsById(testTask1.getId())).thenReturn(false);
        assertThrows(RuntimeException.class, () -> taskServices.deleteTask(testTask1.getId()));
        verify(repository, never()).deleteById(testTask1.getId());
    }

    @Test
    public void shouldUpdateTask() {
        when(repository.findById(testTask1.getId())).thenReturn(Optional.of(testTask1));
        taskServices.updateTask(testTask1.getId(), updateTask);
        verify(repository, times(1)).save(any(Task.class));
    }

    @Test
    public void shouldFailUpdateTaskWhenIdIsNotFound() {
        when(repository.findById(testTask1.getId())).thenReturn(null);
        assertThrows(RuntimeException.class, () -> taskServices.updateTask(testTask1.getId(), updateTask));
        verify(repository, never()).save(any(Task.class));
    }

    @Test
    public void shouldFailUpdateTaskWhenTitleIsNull() {
        UpdateTaskRequestDto updateTask = new UpdateTaskRequestDto(null, "description", "priority", "status");
        when(repository.save(any(Task.class))).thenThrow(new RuntimeException("Title is required"));
        assertThrows(RuntimeException.class, () -> taskServices.updateTask(testTask1.getId(), updateTask));
        verify(repository, never()).save(any(Task.class));
    }

    @Test
    public void shouldFailUpdateTaskWhenTitleIsEmpty() {
        UpdateTaskRequestDto updateTask = new UpdateTaskRequestDto("", "description", "priority", "status");
        when(repository.save(any(Task.class))).thenThrow(new RuntimeException("Title is required"));
        assertThrows(RuntimeException.class, () -> taskServices.updateTask(testTask1.getId(), updateTask));
        verify(repository, never()).save(any(Task.class));
    }

    @Test
    public void shouldFailUpdateTaskWhenDescriptionIsNull() {
        UpdateTaskRequestDto updateTask = new UpdateTaskRequestDto("title", null, "priority", "status");
        when(repository.save(any(Task.class))).thenThrow(new RuntimeException("Description is required"));
        assertThrows(RuntimeException.class, () -> taskServices.updateTask(testTask1.getId(), updateTask));
        verify(repository, never()).save(any(Task.class));
    }

    @Test
    public void shouldFailUpdateTaskWhenDescriptionIsEmpty() {
        UpdateTaskRequestDto updateTask = new UpdateTaskRequestDto("title", "", "priority", "status");
        when(repository.save(any(Task.class))).thenThrow(new RuntimeException("Description is required"));
        assertThrows(RuntimeException.class, () -> taskServices.updateTask(testTask1.getId(), updateTask));
        verify(repository, never()).save(any(Task.class));
    }

    @Test
    public void shouldFailUpdateTaskWhenPriorityIsNull() {
        UpdateTaskRequestDto updateTask = new UpdateTaskRequestDto("title", "description", null, "status");
        when(repository.save(any(Task.class))).thenThrow(new RuntimeException("Priority is required"));
        assertThrows(RuntimeException.class, () -> taskServices.updateTask(testTask1.getId(), updateTask));
        verify(repository, never()).save(any(Task.class));
    }

    @Test
    public void shouldFailUpdateTaskWhenPriorityIsEmpty() {
        UpdateTaskRequestDto updateTask = new UpdateTaskRequestDto("title", "description", "", "status");
        when(repository.save(any(Task.class))).thenThrow(new RuntimeException("Priority is required"));
        assertThrows(RuntimeException.class, () -> taskServices.updateTask(testTask1.getId(), updateTask));
        verify(repository, never()).save(any(Task.class));
    }

    @Test
    public void shouldFailUpdateTaskWhenStatusIsNull() {
        UpdateTaskRequestDto updateTask = new UpdateTaskRequestDto("title", "description", "description", null);
        when(repository.save(any(Task.class))).thenThrow(new RuntimeException("Status is required"));
        assertThrows(RuntimeException.class, () -> taskServices.updateTask(testTask1.getId(), updateTask));
        verify(repository, never()).save(any(Task.class));
    }

    @Test
    public void shouldFailUpdateTaskWhenStatusIsEmpty() {
        UpdateTaskRequestDto updateTask = new UpdateTaskRequestDto("title", "description", "priority", "");
        when(repository.save(any(Task.class))).thenThrow(new RuntimeException("Status is required"));
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