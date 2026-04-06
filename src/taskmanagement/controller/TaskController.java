package taskmanagement.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import taskmanagement.dto.TaskDTO;
import taskmanagement.model.Task;
import taskmanagement.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskController {
    private final TaskService service;

    public TaskController(TaskService taskService) {
        this.service = taskService;
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDTO>> getTasks(@RequestParam(required = false) String author) {
        return author == null ?
                new ResponseEntity<>(service.getAllTasks(), HttpStatus.OK) :
                new ResponseEntity<>(service.getAuthorsTasks(author), HttpStatus.OK);
    }

    @PostMapping("/tasks")
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody Task request, Authentication auth) {
        TaskDTO taskDTO = TaskDTO.toDTO(service.addTask(request, auth.getName()));
        return new ResponseEntity<>(taskDTO, HttpStatus.OK);
    }
}
