package taskmanagement.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import taskmanagement.dto.CommentDTO;
import taskmanagement.dto.CommentProjection;
import taskmanagement.dto.TaskDTO;
import taskmanagement.entity.Comment;
import taskmanagement.enums.TaskStatus;
import taskmanagement.entity.AppUser;
import taskmanagement.entity.Task;
import taskmanagement.repository.AppUserRepo;
import taskmanagement.repository.CommentRepo;
import taskmanagement.repository.TaskRepo;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepo taskRepo;
    private final AppUserRepo userRepo;
    private final CommentRepo commentRepo;

    public Task addTask(Task request, String authorEmail) {
        AppUser author = userRepo.findByEmail(authorEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        request.setAuthor(author);
        return taskRepo.save(request);
    }

    public List<TaskDTO> getAllTasks() {
        return TaskDTO.toDTOList(taskRepo.findAllByOrderByIdDesc());
    }

    public List<TaskDTO> getAuthorsTasks(String authorEmail) {
        return TaskDTO.toDTOList(taskRepo.findByAuthorEmailOrderByIdDesc(authorEmail));
    }

    public List<TaskDTO> getAssigneesTasks(String assigneeEmail) {
        return TaskDTO.toDTOList(taskRepo.findByAssigneeEmailOrderByIdDesc(assigneeEmail));
    }

    public List<TaskDTO> getAuthorsAndAssigneesTasks(String authorEmail, String assigneeEmail) {
        return TaskDTO.toDTOList(taskRepo.findByAuthorEmailAndAssigneeEmailOrderByIdDesc(authorEmail, assigneeEmail));
    }

    public TaskDTO assignTask(Long taskId, String assigneeEmail, Authentication authentication) {
        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
        boolean isTaskAuthor = authentication.getName().equals(task.getAuthor().getEmail());

        if(!isTaskAuthor){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the author can assign the task");
        }

        if (assigneeEmail.equals("none")) {
            task.setAssignee(null);
        } else {
            AppUser assignee = userRepo.findByEmail(assigneeEmail)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
            task.setAssignee(assignee);
        }

        return TaskDTO.toDTO(taskRepo.save(task));
    }

    public TaskDTO updateStatus(Long taskId, TaskStatus status, Authentication auth) {
        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        boolean isAuthor = auth.getName().equals(task.getAuthor().getEmail());
        boolean isAssignee = auth.getName().equals(task.getAssigneeStr());

        if (!isAuthor && !isAssignee)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the author or assignee can update the task status");

        task.setStatus(status);
        return TaskDTO.toDTO(taskRepo.save(task));
    }

    public CommentDTO addComment(Long taskId, String commentText, Authentication auth) {
        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        AppUser author = userRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Comment comment = Comment.builder()
                .comment(commentText)
                .task(task)
                .author(author)
                .build();

        commentRepo.save(comment);

        return new CommentDTO(
                comment.getId().toString(),
                taskId.toString(),
                comment.getComment(),
                author.getEmail()
                );
    }
//
    public List<CommentDTO> getComments(Long taskId) {
        return CommentDTO.toDTOList(commentRepo.findByTaskIdOrderByIdDesc(taskId));
    }

}
