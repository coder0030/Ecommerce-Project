package sumitproject.SpringCart.Security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import sumitproject.SpringCart.Entity.User;
import sumitproject.SpringCart.Helper.AuthProviderType;
import sumitproject.SpringCart.Helper.Role;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AuthUtil {

    @Value("${JWT_SECRET_KEY:}")
    private String jwtSecretKey;

    private SecretKey getJwtSecretKey() {
            return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("Roles", user.getRoles()
                .stream()
                .map(Role::name)
                .collect(Collectors.toSet()));

        return Jwts.builder()
                .subject(user.getUsername())
                .claims(claims)
                .claim("Id", user.getId())
                .signWith(getJwtSecretKey())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
                .compact();
    }

    private Claims getClaim(String token) {
        return Jwts.parser()
                .verifyWith(getJwtSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUsernameFromToken(String token) {
        return getClaim(token).getSubject();
    }

    public List<String> getRolesFromToken(String token) {
        Claims claims = getClaim(token);
        Object rolesObj = claims.get("Roles");

        if (rolesObj == null) {
            return Collections.emptyList();
        }

        if (rolesObj instanceof List<?>) {
            return (List<String>) rolesObj;
        }

        if (rolesObj instanceof Set<?>) {
            return new ArrayList<>((Set<String>) rolesObj);
        }

        return Collections.emptyList();
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = getClaim(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Invalid token: {}", e.getMessage());
            return false;
        }
    }

    public AuthProviderType getProviderType(String registrationId) {
         return switch (registrationId.toLowerCase()) {
             case "google" -> AuthProviderType.GOOGLE;
             case "github" -> AuthProviderType.GITHUB;
             default -> throw new IllegalArgumentException(
                     "Unsupported OAuth2 provider: " + registrationId);
         };
    }

    public String determineProviderIdFromOAuth2User(OAuth2User oAuth2User, String registrationId) {
        String providerType = registrationId.toLowerCase();

        String providerId = switch (providerType) {
            case "google" -> oAuth2User.getAttribute("sub");
            case "github" -> oAuth2User.getAttribute("id");
            default -> throw new IllegalArgumentException(
                    "Unsupported OAuth2 provider: " + registrationId);
        };

        if(providerId == null || providerId.isBlank()) {
            providerId = oAuth2User.getAttribute("email");
        }

        return providerId;
    }
}
