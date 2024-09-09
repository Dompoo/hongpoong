package Dompoo.Hongpoong.common.security;

import Dompoo.Hongpoong.common.exception.impl.AccessDeniedException;
import Dompoo.Hongpoong.common.security.annotation.Secured;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

@Aspect
@Component
public class AccessLevelCheckAspect {
	
	@Before("@annotation(Dompoo.Hongpoong.common.security.annotation.Secured)")
	public void checkAccessLevel(JoinPoint joinPoint) {
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Method method = methodSignature.getMethod();
		Secured secured = method.getAnnotation(Secured.class);
		
		if (secured == null) {
			throw new IllegalStateException("Secured 애노테이션이 없습니다.");
		}
		
		UserClaims userClaims = getUserClaimsFromRequest();
		if (userClaims == null) {
			throw new IllegalStateException("UserClaims가 설정되지 않았습니다.");
		}
		
		if (!userClaims.getRole().hasAccessLevelOf(secured.value())) {
			throw new AccessDeniedException();
		}
	}
	
	private UserClaims getUserClaimsFromRequest() {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		return (UserClaims) attributes.getRequest().getAttribute(AuthInterceptor.ATTRIBUTE_KEY);
	}
}
