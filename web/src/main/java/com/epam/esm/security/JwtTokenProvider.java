package com.epam.esm.security;

import com.epam.esm.util.MessageProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Log4j2
@Component
public class JwtTokenProvider {

    private static final String BEARER_AUTH_PREFIX = "Bearer ";

    @Value("${jwt.token.secret}")
    private String tokenSecret;

    @Value("${jwt.token.expired}")
    private Long tokenLifetime;

    private MessageProvider messageProvider;
    private UserDetailsService userDetailsService;

    @Autowired
    public JwtTokenProvider(MessageProvider messageProvider, UserDetailsService userDetailsService) {
        this.messageProvider = messageProvider;
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    public void init() {
        tokenSecret = Base64.getEncoder().encodeToString(tokenSecret.getBytes());
    }

    public String generateToken(String username) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + tokenLifetime);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, tokenSecret)
                .compact();
    }

    public Optional<String> resolveToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION);
        if(authHeader != null && authHeader.startsWith(BEARER_AUTH_PREFIX)) {
            return Optional.of(authHeader.substring(BEARER_AUTH_PREFIX.length()));
        }
        return Optional.empty();
    }

    public boolean validateToken(String token) {
        try {
            Date now = new Date();
            Date expiration = parseClaim(token, Claims::getExpiration);
            return !expiration.before(now);
        } catch (JwtException | IllegalArgumentException e) {
            log.debug("Token is invalid or expired", e);
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        String username = parseClaim(token, Claims::getSubject);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private <T> T parseClaim(String token, Function<Claims, T> resolver) {
        Claims claims = Jwts.parser()
                .setSigningKey(tokenSecret)
                .parseClaimsJws(token)
                .getBody();
        return resolver.apply(claims);
    }
}
