package Dompoo.Hongpoong.common.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {
	private final JwtProvider jwtProvider;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (request.getMethod().equals("OPTIONS")) return true;
		if (!(handler instanceof HandlerMethod)) return HandlerInterceptor.super.preHandle(request, response, handler);
		if (((HandlerMethod) handler).hasMethodAnnotation(Secured.class)) {
			String baererToken = request.getHeader("Authorization");
			String token;
			if (baererToken != null && baererToken.startsWith("Bearer ")) {
				token = baererToken.substring("Baerer ".length());
			} else {
				token = null;
			}
			UserClaims userClaims = jwtProvider.resolveAccessToken(token);
			request.setAttribute("claims", userClaims);
		}
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
}
