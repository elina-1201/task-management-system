package taskmanagement.dto;

import taskmanagement.enums.TaskStatus;

public interface TaskProjection {
    Long getId();
    String getTitle();
    String getDescription();
    TaskStatus getStatus();
    String getAuthor();
    String getAssignee();
    Long getComments();
}
