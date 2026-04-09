package taskmanagement.requestbody;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AssignRequest(@Email @NotBlank String assignee) {
}
