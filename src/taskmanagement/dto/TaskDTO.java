package taskmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import taskmanagement.entity.Task;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    private String id;
    private String title;
    private String description;
    private String status;
    private String author;
    private String assignee;

    public static TaskDTO toDTO(Task task){
        return new TaskDTO(
                String.valueOf(task.getId()),
                task.getTitle(),
                task.getDescription(),
                task.getStatus().name(),
                task.getAuthor().getEmail().toLowerCase(),
                task.getAssigneeStr()
        );
    }

    public static List<TaskDTO> toDTOList(Iterable<Task> tasks){
        List<TaskDTO> dtos = new ArrayList<>();
        tasks.forEach(task -> dtos.add(toDTO(task)));
        return dtos;
    }
}
