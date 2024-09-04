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
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
public class AccessLevelCheckAspect {
	
	private final MemberRepository memberRepository;
	
	@Before("@annotation(secured)")
	public void checkAccessLevel(JoinPoint joinPoint, Secured secured) {
		UserClaims userClaims = getUserClaimsFromRequest();
		
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
