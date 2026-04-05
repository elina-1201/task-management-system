package taskmanagement.repository;

import org.springframework.data.repository.CrudRepository;
import taskmanagement.model.AppUser;

import java.util.Optional;

public interface AppUserRepo extends CrudRepository<AppUser, Long> {
    boolean existsByEmail(String email);
    Optional<AppUser> findByEmail(String email);
}
