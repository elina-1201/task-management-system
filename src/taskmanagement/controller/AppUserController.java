package taskmanagement.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import taskmanagement.model.AppUser;
import taskmanagement.service.AppUserService;

@RestController
@RequestMapping("/api")
public class AppUserController {
    private final AppUserService service;

    public AppUserController(AppUserService appUserService) {
        this.service = appUserService;
    }

    @PostMapping("/accounts")
    public ResponseEntity<AppUser> createAccount(@Valid @RequestBody AppUser request) {
        try {
            service.addUser(request);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ResponseStatusException e) {
            return switch ((HttpStatus) e.getStatusCode()) {
                case CONFLICT -> ResponseEntity.status(HttpStatus.CONFLICT).build();
                default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            };
        }
    }
}
