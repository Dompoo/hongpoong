package Dompoo.Hongpoong.api.dto.member.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
public class MemberEditRequest {

    @NotBlank(message = "이름은 비어있을 수 없습니다.")
    private final String username;
    @NotBlank(message = "비밀번호는 비어있을 수 없습니다.")
    private final String password;
    
    public MemberEditDto toDto() {
        return MemberEditDto.builder()
                .name(username)
                .password(password)
                .build();
    }
}
