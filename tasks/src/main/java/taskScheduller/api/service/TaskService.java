package taskScheduller.api.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import taskScheduller.api.domain.task.NewTaskRequestDto;
import taskScheduller.api.domain.task.Task;
import taskScheduller.api.domain.task.UpdateTaskRequestDto;
import taskScheduller.api.mapper.TaskMapper;
import taskScheduller.api.repository.TaskRepository;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository repository;

    private TaskMapper mapper;

    public Task saveTask(NewTaskRequestDto task) {
        Task newTask = mapper.toEntity(task);
        try {
            return repository.save(newTask);
        } catch (Exception e) {
            throw new RuntimeException("Error saving task to database: {0}", e);
        }
    }

    public Task getTaskById(UUID id) {
        Task task = repository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Task not found"));

        return task;
    }

    public void deleteTask(UUID id) {
        boolean exists = repository.existsById(id);
        if (!exists) {
            throw new RuntimeException("Task not found");
        }
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting task from database: {0}", e);
        }
    }

    public void updateTask(UUID id, UpdateTaskRequestDto task) {
        Task existingTask = repository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Task not found"));

        if (task.title() != null) {
            existingTask.setTitle(task.title());
        }

        if (task.description() != null) {
            existingTask.setDescription(task.description());
        }

        if (task.priority() != null) {
            existingTask.setPriority(task.priority());
        }
        if (task.status() != null) {
            existingTask.setStatus(task.status());
        }

        try {
            repository.save(existingTask);
        } catch (Exception e) {
            throw new RuntimeException("Error updating task from database: {0}", e);
        }
    }

    public Page<Task> getAllTasksByUserId(UUID userId, int page, int size, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equals("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        PageRequest pageable = PageRequest.of(page, size, sort);

        Page<Task> tasks = repository.findAllByUserId(userId, pageable);

        return tasks;
    }

    public Page<Task> getAllTasksByUserIdFiltered(UUID userId, int page, int size, String searchTerm, String sortBy,
            String sortDirection) {
        Sort sort = sortDirection.equals("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        PageRequest pageable = PageRequest.of(page, size, sort);

        Page<Task> tasks = repository.findAllByUserIdFiltered(userId, pageable, searchTerm);

        return tasks;
    }
}