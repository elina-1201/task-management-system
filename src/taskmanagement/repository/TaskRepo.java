package taskmanagement.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import taskmanagement.dto.TaskProjection;
import taskmanagement.entity.Task;

import java.util.List;

public interface TaskRepo extends CrudRepository<Task, Long> {
    @Query("""
        select\s
            t.id as id,
            t.title as title,
            t.description as description,
            t.status as status,
            lower(a.email) as author,
            lower(s.email) as assignee,
            count(c.id) as comments
        from Task t
        join t.author a
        left join t.assignee s
        left join t.comments c
        where(:authorEmail is null or lower(a.email) = lower(:authorEmail))
          and (:assigneeEmail is null or lower(s.email) = lower(:assigneeEmail))
        group by t.id, t.title, t.description, t.status, a.email, s.email
        order by t.id desc
   \s""")
    List<TaskProjection> findProjection(
            @Param("authorEmail") String authorEmail,
            @Param("assigneeEmail") String assigneeEmail
    );

    default List<TaskProjection> findAllDesc(){
        return findProjection(null, null);
    };

    default List<TaskProjection> findByAuthorDesc(String email){
        return findProjection(email, null);
    };

    default List<TaskProjection> findByAssigneeDesc(String email){
        return findProjection(null, email);
    };

    default List<TaskProjection> findByAuthorAndAssigneeDesc(String authorEmail, String assigneeEmail){
        return findProjection(authorEmail, assigneeEmail);
    }
}
