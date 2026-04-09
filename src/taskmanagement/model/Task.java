package taskmanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import taskmanagement.enums.TaskStatus;

@Data
@Entity
public class Task {
    @Id
    @GeneratedValue
    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.CREATED;

    @JsonIgnore
    @Getter(AccessLevel.NONE)
    @ManyToOne
    @JoinColumn(name="assignee_id")
    private AppUser assignee;

    @ManyToOne
    @JoinColumn(name="user_id")
    private AppUser author;

    @JsonProperty("assignee")
    public String getAssigneeStr() {
        return assignee == null ? "none" : assignee.getEmail();
    }
}
