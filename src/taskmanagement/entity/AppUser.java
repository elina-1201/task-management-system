package taskmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class AppUser {
    @Id
    @GeneratedValue
    private Long id;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 6)
    private String password;

    @OneToMany(mappedBy = "author")
    private List<Task> tasks;

    @OneToMany(mappedBy = "author")
    private List<Comment> comments;
}
