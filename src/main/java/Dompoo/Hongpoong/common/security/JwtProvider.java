package Dompoo.Hongpoong.common.security;

import Dompoo.Hongpoong.common.exception.impl.LoginExpiredException;
import Dompoo.Hongpoong.domain.enums.Club;
import Dompoo.Hongpoong.domain.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JwtProvider {
	
	private static final String CLAIMS_ID_PROPERTIES = "memberId";
	private static final String CLAIMS_EMAIL_PROPERTIES = "email";
	private static final String CLAIMS_ROLE_PROPERTIES = "role";
	private static final String CLAIMS_CLUB_PROPERTIES = "club";
	
	private final String secret;
	private final String resetSecret;
	private final long accessTokenExpTime;
	
	public JwtProvider(
			@Value("${jwt.secret}") String secret,
			@Value("${jwt.reset_secret}") String resetSecret,
			@Value("${jwt.expiration_time}") long accessTokenExpTime
	) {
		this.secret = secret;
		this.resetSecret = resetSecret;
		this.accessTokenExpTime = accessTokenExpTime;
	}
	
	public UserClaims resolveAccessToken(String token) {
		SecretKey secretKey = getSecretKey();
		Claims claims = getClaims(token, secretKey);
		
		return UserClaims.builder()
				.id(((Integer) (claims.get(CLAIMS_ID_PROPERTIES))).longValue())
				.email((String) claims.get(CLAIMS_EMAIL_PROPERTIES))
				.role(Role.valueOf((String) claims.get(CLAIMS_ROLE_PROPERTIES)))
				.club(Club.valueOf((String) claims.get(CLAIMS_CLUB_PROPERTIES)))
				.build();
	}
	
	public String resolveResetToken(String token) {
		SecretKey secretKey = getResetKey();
		Claims claims = getClaims(token, secretKey);
		
		return (String) claims.get(CLAIMS_EMAIL_PROPERTIES);
	}
	
	private Claims getClaims(String token, SecretKey secretKey) {
		try {
			return Jwts.parser()
					.verifyWith(secretKey).build()
					.parseSignedClaims(token)
					.getPayload();
		} catch (ExpiredJwtException e) {
			throw new LoginExpiredException();
		} catch (JwtException e) {
			throw new JwtException("Jwt 예외, token: " + token);
		}
	}
	
	public String generateAccessToken(Long id, String email, Role role, Club club) {
		Claims claims = Jwts.claims()
				.add(CLAIMS_ID_PROPERTIES, id)
				.add(CLAIMS_EMAIL_PROPERTIES, email)
				.add(CLAIMS_ROLE_PROPERTIES, role)
				.add(CLAIMS_CLUB_PROPERTIES, club)
				.build();
		
		ZonedDateTime now = ZonedDateTime.now();
		ZonedDateTime tokenValidity = now.plusSeconds(accessTokenExpTime);
		
		return Jwts.builder()
				.claims(claims)
				.issuedAt(Date.from(now.toInstant()))
				.expiration(Date.from(tokenValidity.toInstant()))
				.signWith(getSecretKey())
				.compact();
	}
	
	private SecretKey getSecretKey() {
		return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}
	
	private SecretKey getResetKey() {
		return Keys.hmacShaKeyFor(resetSecret.getBytes(StandardCharsets.UTF_8));
	}
}
