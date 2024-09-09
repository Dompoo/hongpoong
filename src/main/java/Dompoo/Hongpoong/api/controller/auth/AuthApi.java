package Dompoo.Hongpoong.api.controller.auth;

import Dompoo.Hongpoong.api.dto.auth.request.AcceptSignUpRequest;
import Dompoo.Hongpoong.api.dto.auth.request.EmailValidRequest;
import Dompoo.Hongpoong.api.dto.auth.request.LoginRequest;
import Dompoo.Hongpoong.api.dto.auth.request.SignUpRequest;
import Dompoo.Hongpoong.api.dto.auth.response.EmailValidResponse;
import Dompoo.Hongpoong.api.dto.auth.response.LoginResponse;
import Dompoo.Hongpoong.api.dto.auth.response.SignUpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "1. 인증")
public interface AuthApi {
	
	@Operation(summary = "이메일 유효성 체크")
	EmailValidResponse checkEmailValid(
			@RequestBody EmailValidRequest request
	);
	
	@Operation(summary = "회원가입 요청")
	void requestSignup(
			@RequestBody SignUpRequest request
	);
	
	@Operation(summary = "로그인")
	LoginResponse login(
			@RequestBody LoginRequest request
	);
	
	@Operation(summary = "[관리자] 회원가입 요청 승인")
	void acceptSignup(
			@RequestBody AcceptSignUpRequest request
	);
	
	@Operation(summary = "[관리자] 회원가입 요청 리스트 조회")
	List<SignUpResponse> findAllSignup();
}
