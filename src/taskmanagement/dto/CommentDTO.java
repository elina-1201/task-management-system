package taskmanagement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CommentDTO {

    @JsonProperty("id")
    private String id;
    @JsonProperty("task_id")
    private String taskId;
    @JsonProperty("text")
    private String comment;
    @JsonProperty("author")
    private String author;

    public static CommentDTO toDTO(CommentProjection comment) {
        return new CommentDTO(
                String.valueOf(comment.id()),
                String.valueOf(comment.taskId()),
                comment.comment(),
                comment.author()
        );
    }

    public static List<CommentDTO> toDTOList(List<CommentProjection> comments) {
        List<CommentDTO> dtos = new ArrayList<>();
        comments.forEach(comment -> dtos.add(toDTO(comment)));
        return dtos;
    }
}