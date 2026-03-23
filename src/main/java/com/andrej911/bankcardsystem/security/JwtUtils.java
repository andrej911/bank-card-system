package com.andrej911.bankcardsystem.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {

	@Value("${app.jwt.secret}")
	private String jwtSecret;

	@Value("${app.jwt.expiration.ms}")
	private int jwtExpirationMs;

	private SecretKey getSigningKey() {

		return Keys.hmacShaKeyFor(jwtSecret.getBytes());
	}

	public String generateJwtToken(UserDetails userDetails) {
		return Jwts.builder()

				.setSubject(userDetails.getUsername())

				.setIssuedAt(new Date())

				.setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))

				.signWith(getSigningKey(), SignatureAlgorithm.HS256)

				.compact();
	}

	public String getUsernameFromJwtToken(String token) {
		return Jwts.parserBuilder()

				.setSigningKey(getSigningKey()).build()

				.parseClaimsJws(token)

				.getBody()

				.getSubject();
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(authToken);
			return true;

		} catch (SecurityException e) {
			System.err.println("Invalid JWT signature: " + e.getMessage());
		} catch (MalformedJwtException e) {
			System.err.println("Invalid JWT token: " + e.getMessage());
		} catch (ExpiredJwtException e) {
			System.err.println("JWT token is expired: " + e.getMessage());
		} catch (UnsupportedJwtException e) {
			System.err.println("JWT token is unsupported: " + e.getMessage());
		} catch (IllegalArgumentException e) {
			System.err.println("JWT claims string is empty: " + e.getMessage());
		}

		return false;
	}
}