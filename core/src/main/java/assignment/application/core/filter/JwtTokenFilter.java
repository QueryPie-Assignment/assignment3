package assignment.application.core.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

	private final String jwtSecret;
	private final List<RequestMatcher> permitAllRequestMatchers;
	private final RedisTemplate<String, String> blackListTemplate;

	@Bean
	public JwtTokenFilter jwtTokenFilter() {
		List<String> permitAllEndpoints = Arrays.asList(
			// 토큰 검사가 필요 없는 경로 목록
			// Swagger
			"/swagger-ui/**",
			"/v3/api-docs/**",

			// 개방 URL
			// TODO TOKEN URL 설정
			"/member/save",
			"/member/reissue-token",
			"/member/logout"
		);
		return new JwtTokenFilter(jwtSecret, permitAllEndpoints, blackListTemplate);
	}

	public JwtTokenFilter(
		String jwtSecret,
		List<String> permitAllEndpoints,
		RedisTemplate<String, String> blackListTemplate
	) {
		this.jwtSecret = jwtSecret;
		this.permitAllRequestMatchers = permitAllEndpoints.stream()
			.map(AntPathRequestMatcher::new)
			.collect(Collectors.toList());
		this.blackListTemplate = blackListTemplate;
	}

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		// Filter가 적용되고 있는 uri 추출
		String method = request.getMethod();

		// pre-flight 요청일 때, 해당 Filter 건너뜀.
		if (method.equals("OPTIONS")) {
			return;
		}

		// Check if the request matches any permitAll endpoint
		boolean isPermitAllEndpoint = permitAllRequestMatchers.stream()
			.anyMatch(matcher -> matcher.matches(request));

		if (isPermitAllEndpoint) {
			filterChain.doFilter(request, response); // 건너뛰고 다음 필터로 넘어갑니다.
			return;
		}

		String authHeader = request.getHeader("Authorization");

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7); // Remove "Bearer " from the header value

			try {
				// Black List에서 토큰 확인
				if (Boolean.TRUE.equals(blackListTemplate.hasKey(token))) {
					log.error("Blacklisted token detected: {}", token);

					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					return;
				}

				Jws<Claims> claimsJws = Jwts.parser()
					.setSigningKey(jwtSecret)
					.parseClaimsJws(token);

				Claims claims = claimsJws.getBody();
				String username = claims.getSubject();

				UserDetails userDetails = User.withUsername(username)
					.password("")
					.authorities(new ArrayList<>())
					.build();

				Authentication authentication = new UsernamePasswordAuthenticationToken(
					userDetails,
					null,
					userDetails.getAuthorities()
				);
				SecurityContextHolder.getContext().setAuthentication(authentication);

				response.setHeader("Authorization", "Bearer " + token); // Add "Bearer " to the header value
				filterChain.doFilter(request, response);

			} catch (JwtException e) {
				SecurityContextHolder.clearContext();
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}
		} else {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
	}
}