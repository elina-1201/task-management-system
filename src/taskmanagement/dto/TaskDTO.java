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

//    public void setId(Long id) {
//        this.id = String.valueOf(id);
//    }
//
//    public void setStatus(TaskStatus status) {
//        this.status = status.name();
//    }

//    private static TaskDTO build (Long id, String title, String description, TaskStatus status,
//                                 String authorEmail, String assigneeEmail, Long comments) {
//        TaskDTO dto = new TaskDTO();
//        dto.setId(id);
//        dto.setTitle(title);
//        dto.setDescription(description);
//        dto.setStatus(status);
//        dto.setAuthor(authorEmail);
//        dto.setAssignee(assigneeEmail == null ? "none" : assigneeEmail);
//        dto.setComments(comments == null ? 0L : comments);
//        return dto;
//    }

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
