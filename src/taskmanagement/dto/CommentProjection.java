package taskmanagement.dto;

public record CommentProjection(
        Long id,
        Long taskId,
        String comment,
        String author
) {}
