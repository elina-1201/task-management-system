package taskmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import taskmanagement.enums.TaskStatus;

import java.util.ArrayList;
import java.util.List;

@Setter
@Entity
public class Task {
    @Id
    @GeneratedValue
    private Long id;
    public String getId() {
        return String.valueOf(id);
    }

    @Getter
    @NotBlank
    private String title;

    @Getter
    @NotBlank
    private String description;

    @Getter
    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.CREATED;

    @ManyToOne
    @JoinColumn(name="author_id")
    private AppUser author;
    @JsonProperty("author")
    public String getAuthor() {
        return author.getEmail();
    }

    @ManyToOne
    @JoinColumn(name="assignee_id")
    private AppUser assignee;
    @JsonProperty("assignee")
    public String getAssignee() {
        return assignee == null ? "none" : assignee.getEmail();
    }

    @JsonIgnore
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
}
