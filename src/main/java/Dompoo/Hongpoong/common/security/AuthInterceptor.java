package Dompoo.Hongpoong.common.security;

import Dompoo.Hongpoong.common.exception.impl.NotLoginException;
import Dompoo.Hongpoong.common.security.annotation.Secured;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {
	
	private static final String AUTH_TOKEN_HEADER = "Authorization";
	private static final String AUTH_TOKEN_PREFIX = "Bearer ";
	private static final String OPTIONS = "OPTIONS";
	public static final String ATTRIBUTE_KEY = "claims";
	
	private final JwtProvider jwtProvider;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (isPreFlight(request)) return true;
		if (!(handler instanceof HandlerMethod)) return HandlerInterceptor.super.preHandle(request, response, handler);
		if (isSecured(handler)) authenticate(request);
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
	
	private void authenticate(HttpServletRequest request) {
		String baererToken = request.getHeader(AUTH_TOKEN_HEADER);
		String token;
		if (baererToken != null && baererToken.startsWith(AUTH_TOKEN_PREFIX)) {
			token = baererToken.substring(AUTH_TOKEN_PREFIX.length());
		} else {
			token = null;
		}
		if (token == null || token.isBlank()) {
			throw new NotLoginException();
		}
		UserClaims userClaims = jwtProvider.resolveAccessToken(token);
		request.setAttribute(ATTRIBUTE_KEY, userClaims);
	}
	
	private static boolean isSecured(Object handler) {
		return ((HandlerMethod) handler).hasMethodAnnotation(Secured.class);
	}
	
	private static boolean isPreFlight(HttpServletRequest request) {
		return request.getMethod().equals(OPTIONS);
	}
}
