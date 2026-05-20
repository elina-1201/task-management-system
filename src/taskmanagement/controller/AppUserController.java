package taskmanagement.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
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
    private final JwtDecoder jwtDecoder;

    @PostMapping("/accounts")
    public ResponseEntity<?> createAccount(@Valid @RequestBody AppUser request) {
        try {
            service.addUser(request);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

    String getJwtToken(String subject, String type, int expiration, List<String> authorities) {
        var accessClaims = JwtClaimsSet.builder()
                .subject(subject)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(expiration))
                .claim("scope", authorities)
                .claim("token_type", type)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(accessClaims)).getTokenValue();
    }

    @PostMapping("/auth/token")
    public ResponseEntity<Map<String, String>> getToken(Authentication auth) {
        List<String> authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        // access token (short-lived)
        String accessToken = getJwtToken(auth.getName(), "access", 60, authorities);

        // refresh token (longer lived)
        String refreshToken = getJwtToken(auth.getName(), "refresh", 60 * 60 * 24 * 7, authorities);
        return new ResponseEntity<>(new HashMap<>(Map.of(
                "token", accessToken,
                "refresh_token", refreshToken
        )), HttpStatus.OK);
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestBody Map<String, String> body) {
        var incoming = body.get("refresh_token");
        if (incoming == null || incoming.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "refresh_token is required");
        }

        try {
            var jwt = jwtDecoder.decode(incoming);

            Object tt = jwt.getClaim("token_type");
            if (tt == null || !"refresh".equals(tt.toString())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
            }

            var subject = jwt.getSubject();
            List<String> authorities = jwt.getClaimAsStringList("scope");

            // issue new access token
            String accessToken = getJwtToken(subject, "access", 60, authorities);

            // issue new refresh token (rotation)
            String refreshToken = getJwtToken(subject, "refresh",60 * 60 * 24 * 7 , authorities);


            return new ResponseEntity<>(new HashMap<>(Map.of(
                    "token", accessToken,
                    "refresh_token", refreshToken
            )), HttpStatus.OK);
        } catch (JwtException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token", e);
        }
    }
}
