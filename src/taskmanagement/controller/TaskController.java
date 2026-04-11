package taskmanagement.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import taskmanagement.dto.CommentDTO;
import taskmanagement.dto.TaskDTO;
import taskmanagement.entity.Task;
import taskmanagement.repository.CommentRepo;
import taskmanagement.requestbody.AssignRequest;
import taskmanagement.requestbody.CommentRequest;
import taskmanagement.requestbody.StatusUpdateRequest;
import taskmanagement.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class TaskController {
    private final TaskService service;
    private final CommentRepo commentRepo;

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDTO>> getTasks(@RequestParam(required = false) String author,
                                                  @RequestParam(required = false) String assignee) {
        if (author != null && assignee != null) {
            return new ResponseEntity<>(service.getAuthorsAndAssigneesTasks(author.toLowerCase(), assignee.toLowerCase()), HttpStatus.OK);
        }

        if (author != null) {
            return new ResponseEntity<>(service.getAuthorsTasks(author.toLowerCase()), HttpStatus.OK);
        }

        if (assignee != null) {
            return new ResponseEntity<>(service.getAssigneesTasks(assignee.toLowerCase()), HttpStatus.OK);
        }

        return new ResponseEntity<>(service.getAllTasks(), HttpStatus.OK);
    }

    @PostMapping("/tasks")
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task request, Authentication auth) {
        Task task = service.addTask(request, auth.getName());
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @PutMapping("/tasks/{taskId}/assign")
    public ResponseEntity<?> assignTask(@PathVariable Long taskId,
                                        @RequestBody AssignRequest request,
                                        Authentication auth) {
        try {
            Task task = service.assignTask(taskId, request.assignee(), auth);
            return new ResponseEntity<>(task, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

    @PutMapping("/tasks/{taskId}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long taskId,
                                          @Valid @RequestBody StatusUpdateRequest request,
                                          Authentication authentication) {
        try {
            Task task = service.updateStatus(taskId, request.status(), authentication);
            return new ResponseEntity<>(task, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

    @PostMapping("/tasks/{taskId}/comments")
    public ResponseEntity<?> addComment(@PathVariable Long taskId,
                                        @Valid @RequestBody CommentRequest request,
                                        Authentication authentication) {
        try {
            CommentDTO commentDTO = service.addComment(taskId, request.text(), authentication);
            return new ResponseEntity<>(commentDTO, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

    @GetMapping("/tasks/{taskId}/comments")
    public ResponseEntity<?> getComments(@PathVariable Long taskId) {
        try {
            List<CommentDTO> comments = service.getComments(taskId);
            return new ResponseEntity<>(comments, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }
}
