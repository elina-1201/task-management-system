package taskmanagement.repository;

import org.springframework.data.repository.CrudRepository;
import taskmanagement.entity.Task;

public interface TaskRepo extends CrudRepository<Task, Long> {
    Iterable<Task> findAllByOrderByIdDesc();
    Iterable<Task> findByAuthorEmailOrderByIdDesc(String email);
    Iterable<Task> findByAssigneeEmailOrderByIdDesc(String email);
    Iterable<Task> findByAuthorEmailAndAssigneeEmailOrderByIdDesc(String authorEmail, String assigneeEmail);
}
