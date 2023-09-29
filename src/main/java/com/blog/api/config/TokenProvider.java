package com.blog.api.config;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenProvider {

	
	SecretKey key=Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
	
	public String generateToken(Authentication  authentication)
	{
		String jwt=Jwts.builder().setIssuer("Sumit").setIssuedAt(new Date()).setExpiration(new Date(new Date().getTime()+86400000))
				.claim("email", authentication.getName())
				.signWith(key).compact();
		return jwt;
	}
	
	public String getEmailFromToken(String jwt)
	{
		jwt=jwt.substring(7);
		Claims claim=Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
		String email=String.valueOf(claim.get("email"));
		return email;
	}
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}
	
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}
	
	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims=getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}
	
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}
	
	private Boolean isTokenExpired(String token) {
		final Date expiration=getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}
	
	public String tokenGenerate(UserDetails userDetails) {
		Map<String, Object> claims=new HashMap<String, Object>();
		return doGenrateToken(claims, userDetails.getUsername());
	}
	
	private String doGenrateToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date()).setExpiration(new Date(new Date().getTime()+86400000))
				.signWith(key).compact();
	}
	
	public boolean validateToken(String token, UserDetails userDetails) {
		final String username=getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}
