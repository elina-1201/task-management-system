package taskmanagement.service;

import org.springframework.stereotype.Service;
import taskmanagement.dto.TaskDTO;
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
        AppUser author = userRepo.findByEmail(authorEmail).orElseThrow(() -> new RuntimeException("User not found"));
        request.setAuthor(author);
        return taskRepo.save(request);
    }

    public List<TaskDTO> getAllTasks() {
        return TaskDTO.toDTOList(taskRepo.findAll());
    }

    public List<TaskDTO> getAuthorsTasks(String authorEmail) {
        return TaskDTO.toDTOList(taskRepo.findByAuthorEmail(authorEmail));
    }
}
