package Dompoo.Hongpoong.common.security;

import org.springframework.stereotype.Component;

@Component
public class JwtProvider {
	public UserClaims resolveAccessToken(String token) {
		// TODO : token 값을 통해 UserClaims를 Resolve 하는 코드 추가 필요
		return null;
	}
	
	public String generateAccessToken(Long id, String email) {
		// TODO : id, email 기반으로 토큰을 생성하는 코드 추가 필요
		return null;
	}
}
