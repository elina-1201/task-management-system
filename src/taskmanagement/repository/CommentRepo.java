package taskmanagement.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import taskmanagement.dto.CommentProjection;
import taskmanagement.entity.Comment;

import java.util.List;

public interface CommentRepo extends CrudRepository<Comment, Long> {
    @Query("""
            select new taskmanagement.dto.CommentProjection(
                c.id,
                c.task.id,
                c.comment,
                lower(a.email)
            )
            from Comment c
            join c.task t
            join c.author a
            where t.id = :taskId
            order by c.id desc
            """)
    List<CommentProjection> findByTaskIdOrderByIdDesc(Long taskId);
//    Iterable<Comment> findByTaskIdOrderByIdDesc(Long taskId);
}
