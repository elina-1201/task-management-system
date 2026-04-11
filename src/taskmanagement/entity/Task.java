package taskmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import taskmanagement.enums.TaskStatus;

import java.util.ArrayList;
import java.util.List;

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

    @Getter(AccessLevel.NONE)
    @ManyToOne
    @JoinColumn(name="author_id")
    private AppUser author;

    @JsonProperty("author")
    public String getAuthor() {
        return author.getEmail();
    }

    @Getter(AccessLevel.NONE)
    @ManyToOne
    @JoinColumn(name="assignee_id")
    private AppUser assignee;

    @JsonProperty("assignee")
    public String getAssignee() {
        return assignee == null ? "none" : assignee.getEmail();
    }

    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @JsonProperty("total_comments")
    public Long getCommentsCount(){
        return (long) comments.size();
    }
}
