package taskmanagement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import taskmanagement.entity.Comment;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {

    @JsonProperty("id")
    private String id;
    @JsonProperty("task_id")
    private String taskId;
    @JsonProperty("text")
    private String comment;
    @JsonProperty("author")
    private String author;

    public static CommentDTO toDTO(Comment comment) {
        return new CommentDTO(
                String.valueOf(comment.getId()),
                String.valueOf(comment.getTask().getId()),
                comment.getComment(),
                comment.getAuthor().getEmail().toLowerCase()
        );
    }

    public static List<CommentDTO> toDTOList(Iterable<Comment> comments) {
        List<CommentDTO> dtos = new ArrayList<>();
        comments.forEach(comment -> dtos.add(toDTO(comment)));
        return dtos;
    }
}
