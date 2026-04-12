package taskmanagement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import taskmanagement.entity.Task;
import taskmanagement.enums.TaskStatus;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDTO {
    @Setter(AccessLevel.NONE)
    private String id;
    private String title;
    private String description;
    @Setter(AccessLevel.NONE)
    private String status;
    private String author;
    private String assignee;
    @JsonProperty("total_comments")
    private Long comments;

    public static TaskDTO toDTO(TaskProjection task) {
        return new TaskDTO(
                String.valueOf(task.getId()),
                task.getTitle(),
                task.getDescription(),
                task.getStatus().name(),
                task.getAuthor(),
                task.getAssignee(),
                task.getComments()
        );
    }

    public static List<TaskDTO> toDTOList(Iterable<TaskProjection> tasks){
        List<TaskDTO> dtos = new ArrayList<>();
        tasks.forEach(task -> dtos.add(toDTO(task)));
        return dtos;
    }
}
