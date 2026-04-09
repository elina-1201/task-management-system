package taskmanagement.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import taskmanagement.model.AppUser;
import taskmanagement.model.AppUserAdapter;
import taskmanagement.repository.AppUserRepo;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {
    private final AppUserRepo repository;
    private final PasswordEncoder passwordEncoder;

    public void addUser(AppUser request){
        if(repository.existsByEmail(request.getEmail().toLowerCase())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        request.setPassword(encodedPassword);
        repository.save(request);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = repository.findByEmail(username.toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException("User with email: " + username + " not found"));
        return new AppUserAdapter(user);
    }
}
