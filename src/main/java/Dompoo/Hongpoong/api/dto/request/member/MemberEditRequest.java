package Dompoo.Hongpoong.api.dto.request.member;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberEditRequest {

    @NotBlank(message = "이름은 비어있을 수 없습니다.")
    private String username;
    @NotBlank(message = "비밀번호는 비어있을 수 없습니다.")
    private String password;
    
    @Builder
    private MemberEditRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public MemberEditDto toDto() {
        return MemberEditDto.builder()
                .name(username)
                .password(password)
                .build();
    }
}
