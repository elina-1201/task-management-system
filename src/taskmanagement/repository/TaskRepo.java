package taskmanagement.repository;

import org.springframework.data.repository.CrudRepository;
import taskmanagement.model.Task;

public interface TaskRepo extends CrudRepository<Task, Long> {
    Iterable<Task> findByAuthorEmail(String email);
}
