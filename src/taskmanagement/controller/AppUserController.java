package taskmanagement.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import taskmanagement.entity.AppUser;
import taskmanagement.service.AppUserService;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class AppUserController {
    private final AppUserService service;
    private final JwtEncoder jwtEncoder;

    @PostMapping("/accounts")
    public ResponseEntity<?> createAccount(@Valid @RequestBody AppUser request) {
        try {
            service.addUser(request);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

    @PostMapping("/auth/token")
    public ResponseEntity<Map<String, String>> getToken(Authentication auth) {
        List<String> authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        var claimSet = JwtClaimsSet.builder()
                .subject(auth.getName())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(60))
                .claim("scope", authorities)
                .build();

        String tokenValue = jwtEncoder.encode(JwtEncoderParameters.from(claimSet)).getTokenValue();
        return new ResponseEntity<>(new HashMap<>(Map.of("token", tokenValue)), HttpStatus.OK);
    }
}
