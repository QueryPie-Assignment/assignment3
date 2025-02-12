package assignment.application.domain.member;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import assignment.application.exception.message.ErrorResult;
import assignment.application.exception.status.BadRequestException;
import assignment.application.exception.status.UnAuthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberDomain {
	private final RedisTemplate<String, String> redisTemplate;
	private final RedisTemplate<String, String> blackListTemplate;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	@Value("${jwt.secret}")
	private String jwtSecret;
	@Value("${jwt.accessTokenExpiration}")
	private long accessTokenExpiration;
	@Value("${jwt.refreshTokenExpiration}")
	private long refreshTokenExpiration;

	public void checkIdPassword(String inputId, String inputPassword, String id, String encodedPassword) {
		// 아이디가 일치하는지 확인
		if (!inputId.equals(id)) {
			throw new BadRequestException(ErrorResult.ID_PASSWORD_MISMATCH_BAD_REQUEST_EXCEPTION);
		}

		// 비밀번호가 일치하는지 확인 (BCrypt 암호화 검증)
		if (!bCryptPasswordEncoder.matches(inputPassword, encodedPassword)) {
			throw new BadRequestException(ErrorResult.ID_PASSWORD_MISMATCH_BAD_REQUEST_EXCEPTION);
		}
	}

	public String generateAccessToken(String id) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + accessTokenExpiration);

		return Jwts.builder()
			.setSubject(id)
			.setIssuedAt(now)
			.setExpiration(expiryDate)
			.signWith(SignatureAlgorithm.HS512, jwtSecret)
			.compact();
	}

	public String generateRefreshToken(String id) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + refreshTokenExpiration);

		return Jwts.builder()
			.setSubject(id)
			.setIssuedAt(now)
			.setExpiration(expiryDate)
			.signWith(SignatureAlgorithm.HS512, jwtSecret)
			.compact();
	}

	public void saveRefreshToken(String refreshToken, String id) {
		redisTemplate.opsForValue()
			.set(
				id,
				refreshToken,
				refreshTokenExpiration,
				TimeUnit.MILLISECONDS
			);
	}

	public String extractIdFromRequest(HttpServletRequest request) {
		// Authorization 헤더 가져오기
		String authorizationHeader = request.getHeader("Authorization");

		// Authorization 헤더가 비어있거나 "Bearer "로 시작하지 않는 경우 예외 처리
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			throw new UnAuthorizedException(ErrorResult.TOKEN_UNAUTHORIZED_EXCEPTION);
		}

		// "Bearer " 이후의 순수한 JWT 토큰 추출
		String token = authorizationHeader.substring(7).trim();

		try {
			// JWT 토큰 파싱하여 Subject(ID) 추출
			Claims claims = Jwts.parser()
				.setSigningKey(jwtSecret)
				.parseClaimsJws(token)
				.getBody();

			String id = claims.getSubject();

			// ID가 정상적으로 추출되지 않았다면 예외 처리
			if (id == null || id.isEmpty()) {
				throw new UnAuthorizedException(ErrorResult.TOKEN_UNAUTHORIZED_EXCEPTION);
			}

			return id;

		} catch (JwtException | IllegalArgumentException e) {
			throw new UnAuthorizedException(ErrorResult.TOKEN_UNAUTHORIZED_EXCEPTION);
		}
	}
}
