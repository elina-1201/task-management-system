package taskmanagement.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import taskmanagement.dto.TaskDTO;
import taskmanagement.enums.TaskStatus;
import taskmanagement.model.AppUser;
import taskmanagement.model.Task;
import taskmanagement.repository.AppUserRepo;
import taskmanagement.repository.TaskRepo;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepo taskRepo;
    private final AppUserRepo userRepo;

    public TaskService(TaskRepo repository, AppUserRepo userRepo) {
        this.taskRepo = repository;
        this.userRepo = userRepo;
    }

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
//        List<TaskDTO> authorsTasks = TaskDTO.toDTOList(taskRepo.findByAuthorEmailOrderByIdDesc(authorEmail));
//        List<TaskDTO> assigneesTasks = TaskDTO.toDTOList(taskRepo.findByAssigneeEmailOrderByIdDesc(assigneeEmail));
//        return authorsTasks.stream()
//                .filter(assigneesTasks::contains)
//                .toList();
    }

    public TaskDTO assignTask(Long taskId, String assigneeEmail) {
        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
        AppUser assignee = userRepo.findByEmail(assigneeEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        task.setAssignee(assignee);

        return TaskDTO.toDTO(taskRepo.save(task));
    }

    public TaskDTO updateStatus(Long taskId, TaskStatus status, Authentication auth) {
        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        boolean isAuthor = auth.getName().equals(task.getAuthor().getEmail());
        boolean isAssignee = auth.getName().equals(task.getAssigneeStr());

        if(!isAuthor && !isAssignee) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the author or assignee can update the task status");

        task.setStatus(status);
        return TaskDTO.toDTO(taskRepo.save(task));
    }
}
