package taskscheduller.api.domain.task;

import jakarta.validation.constraints.NotNull;

public record UpdateTaskRequestDto(
        @NotNull(message = "Title is required") String title,
        @NotNull(message = "Description is required") String description,
        @NotNull(message = "Priority is required") String priority,
        @NotNull(message = "Status is required") String status) {
}