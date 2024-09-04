package Dompoo.Hongpoong.common.security;

import Dompoo.Hongpoong.common.exception.impl.AccessDeniedException;
import Dompoo.Hongpoong.common.exception.impl.MemberNotFound;
import Dompoo.Hongpoong.common.security.annotation.Secured;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

@Aspect
@RequiredArgsConstructor
public class AccessLevelCheckAspect {
	
	private final MemberRepository memberRepository;
	
	@Before("@annotation(Dompoo.Hongpoong.common.security.annotation.Secured)")
	public void checkAccessLevel(JoinPoint joinPoint) {
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Method method = methodSignature.getMethod();
		Secured secured = method.getAnnotation(Secured.class);
		if (secured == null) {
			throw new IllegalStateException("Secured annotation is not present on the method");
		}
		
		UserClaims userClaims = getUserClaimsFromRequest();
		if (userClaims == null) {
			throw new IllegalStateException("userClaims is not present on the method");
		}
		
		Member member = memberRepository.findByIdAndEmail(userClaims.getId(), userClaims.getEmail())
				.orElseThrow(MemberNotFound::new);
		
		if (!member.hasAccessLevel(secured.value())) {
			throw new AccessDeniedException();
		}
	}
	
	private UserClaims getUserClaimsFromRequest() {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		return (UserClaims) attributes.getRequest().getAttribute(AuthInterceptor.ATTRIBUTE_KEY);
	}
}