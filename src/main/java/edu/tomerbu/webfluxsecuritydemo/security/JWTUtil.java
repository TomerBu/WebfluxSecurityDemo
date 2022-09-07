package edu.tomerbu.webfluxsecuritydemo.security;

import edu.tomerbu.webfluxsecuritydemo.entitiy.User;
import edu.tomerbu.webfluxsecuritydemo.error.InvalidJWTException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtil {

    @Value("${edu.tomerbu.jwt.secret}")
    private String secret;

    @Value("${edu.tomerbu.jwt.expiration}")
    private String expirationTime;

    private Key key;

    @PostConstruct
    //executed after dependency injection is done
    //to perform any initialization before being used
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key).build()
                    .parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException | UnsupportedJwtException |
                 MalformedJwtException | SignatureException |
                 IllegalArgumentException e) {
            throw new InvalidJWTException(e);
        }
    }

    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRoles());
        return doGenerateToken(claims, user.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims,
                                   String username) {
        long expirationTimeLong = Long.parseLong(expirationTime);//millis
        final Date createdDate = new Date();
        final Date expirationDate = new Date(
                createdDate.getTime() + expirationTimeLong * 1000
        );

        try {
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(username)
                    .setIssuedAt(createdDate)
                    .setExpiration(expirationDate)
                    .signWith(key)
                    .compact();
        } catch (InvalidKeyException e) {
            throw new InvalidJWTException(e);
        }
    }
}