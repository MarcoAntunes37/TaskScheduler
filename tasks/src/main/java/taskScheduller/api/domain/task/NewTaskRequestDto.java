package taskScheduller.api.domain.task;

import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record NewTaskRequestDto(
        @NotNull(message = "UserId is required") UUID userId,
        @NotNull(message = "Title is required") @NotEmpty(message = "Title is required") String title,
        @NotNull(message = "Description is required") @NotEmpty(message = "Description is required") String description,
        @NotNull(message = "Priority is required") @NotEmpty(message = "Priority is required") String priority,
        @NotNull(message = "Status is required") @NotEmpty(message = "Status is required") String status) {
}
