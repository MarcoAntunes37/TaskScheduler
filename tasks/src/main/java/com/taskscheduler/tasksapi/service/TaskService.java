package com.taskscheduler.tasksapi.service;

import java.util.UUID;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.taskscheduler.tasksapi.domain.task.NewTaskRequestDto;
import com.taskscheduler.tasksapi.domain.task.Task;
import com.taskscheduler.tasksapi.domain.task.UpdateTaskRequestDto;
import com.taskscheduler.tasksapi.mapper.TaskMapper;
import com.taskscheduler.tasksapi.repository.TaskRepository;

import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository repository;

    @Autowired
    private TaskMapper mapper = Mappers.getMapper(TaskMapper.class);

    public Task saveTask(NewTaskRequestDto task) {
        Task newTask = mapper.toEntity(task);

        try {
            return repository.save(newTask);
        } catch (Exception e) {
            throw new PersistenceException("Error saving task to database:" + e);
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
            throw new IllegalArgumentException("Task not found");
        }
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting task from database: {0}", e);
        }
    }

    public Task updateTask(UUID id, UpdateTaskRequestDto task) {
        Task existingTask = repository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Task not found"));

        existingTask.setTitle(task.title());
        existingTask.setDescription(task.description());
        existingTask.setPriority(task.priority());
        existingTask.setStatus(task.status());

        try {
            repository.save(existingTask);

            return existingTask;
        } catch (Exception e) {
            throw new RuntimeException("Error updating task from database: {0}", e);
        }
    }

    public Page<Task> getAllTasks(UUID userId, int page, int size, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equals("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        PageRequest pageable = PageRequest.of(page, size, sort);

        Page<Task> tasks = repository.findAllByUserId(userId, pageable);

        return tasks;
    }

    public Page<Task> getAllTasksFiltered(UUID userId, int page, int size, String searchTerm, String sortBy,
            String sortDirection) {
        Sort sort = sortDirection.equals("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        PageRequest pageable = PageRequest.of(page, size, sort);

        Page<Task> tasks = repository.findAllByUserIdFiltered(userId, pageable, searchTerm);

        return tasks;
    }
}