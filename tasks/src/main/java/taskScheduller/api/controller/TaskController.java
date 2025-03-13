package taskscheduller.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import taskscheduller.api.domain.task.NewTaskRequestDto;
import taskscheduller.api.domain.task.Task;
import taskscheduller.api.domain.task.UpdateTaskRequestDto;
import taskscheduller.api.service.TaskService;

import java.net.URI;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskServices;

    @PostMapping
    public ResponseEntity<Task> createTask(
            @RequestBody @Valid NewTaskRequestDto task) {
        Task newTask = this.taskServices.saveTask(task);

        return ResponseEntity
                .created(URI.create("/api/tasks"))
                .body(newTask);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable String id) {
        Task task = taskServices.getTaskById(UUID.fromString(id));

        return ResponseEntity.ok(task);
    }

    @GetMapping("/all/{userId}")
    public ResponseEntity<Page<Task>> getAllTasks(
            @PathVariable String userId,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sortBy,
            @RequestParam String sortDirection) {
        Page<Task> tasks = taskServices.getAllTasks(
                UUID.fromString(userId),
                page,
                size,
                sortBy,
                sortDirection);

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/all/{userId}/filtered")
    public ResponseEntity<Page<Task>> getAllTasksFiltered(
            @PathVariable String userId,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String searchTerm,
            @RequestParam String sortBy,
            @RequestParam String sortDirection) {
        Page<Task> tasks = taskServices.getAllTasksFiltered(
                UUID.fromString(userId),
                page,
                size,
                searchTerm,
                sortBy,
                sortDirection);

        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateTaskById(
            @PathVariable UUID id,
            @RequestBody UpdateTaskRequestDto entity) {
        taskServices.updateTask(id, entity);
        return ResponseEntity.ok("Task updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTaskById(@PathVariable UUID id) {
        taskServices.deleteTask(id);
        return ResponseEntity.ok("Task deleted successfully");
    }
}