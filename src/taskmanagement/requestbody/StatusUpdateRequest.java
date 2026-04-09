package taskmanagement.requestbody;

import jakarta.validation.constraints.NotNull;
import taskmanagement.enums.TaskStatus;

public record StatusUpdateRequest(@NotNull TaskStatus status) {
}
