package com.ledgerhub.config.jwt;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtTokenUtil implements Serializable {

	private static final long serialVersionUID = -2550185165626007488L;

	// 5 hours
	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60 * 1000;

	@Value("${jwt.secret}")
	private String secret;

	private SecretKey secretKey;

	@PostConstruct
	public void init() {
		// ✅ Strong key handling (HS512 requires >= 64 bytes)
		this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}

	/*
	 * ========================= Token parsing =========================
	 */

	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public Date getIssuedAtDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getIssuedAt);
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
	}

	/*
	 * ========================= Token validation =========================
	 */

	private boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	public boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}

	/*
	 * ========================= Token generation =========================
	 */

	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, userDetails.getUsername());
	}

	private String doGenerateToken(Map<String, Object> claims, String subject) {

		Date now = new Date();
		Date expiry = new Date(now.getTime() + JWT_TOKEN_VALIDITY);

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(now).setExpiration(expiry)
				.signWith(secretKey, SignatureAlgorithm.HS512).compact();
	}

	/*
	 * ========================= Optional refresh support =========================
	 */

	public boolean canTokenBeRefreshed(String token) {
		return !isTokenExpired(token);
	}
}
