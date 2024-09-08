package Dompoo.Hongpoong.api.dto.auth.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginResponse {
	
	@Schema(example = "90wcr0wri0x9i23m09rz01z")
	private final String token;
}
