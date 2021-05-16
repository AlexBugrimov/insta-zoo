package dev.bug.zoobackend.security;

import dev.bug.zoobackend.entity.User;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static dev.bug.zoobackend.security.SecurityConstants.EXPIRATION_TIME;
import static dev.bug.zoobackend.security.SecurityConstants.SECRET;

@Slf4j
@Component
public class JwtTokenProvider {

    public String generateToken(Authentication authentication) {
        var user = (User) authentication.getPrincipal();
        var now = Date.from(Instant.now());
        var expiryDate = Date.from(Instant.ofEpochMilli(now.getTime() + EXPIRATION_TIME));
        var userId = Long.toString(user.getId());
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("id", userId);
        claimsMap.put("username", user.getEmail());
        claimsMap.put("firstname", user.getName());
        claimsMap.put("lastname", user.getLastname());
        return Jwts.builder()
                .setSubject(userId)
                .addClaims(claimsMap)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJwt(token);
            return true;
        } catch (SignatureException |
                MalformedJwtException |
                ExpiredJwtException |
                UnsupportedJwtException |
                IllegalArgumentException ex) {
            log.error(ex.getMessage());
            return false;
        }
    }

    public Long getUserIdFromToken(String token) {
        var claims = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJwt(token)
                .getBody();
        String userId = (String) claims.get("id");
        return Long.parseLong(userId);
    }
}
