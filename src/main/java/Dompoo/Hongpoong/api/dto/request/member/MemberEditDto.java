package Dompoo.Hongpoong.api.dto.request.member;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberEditDto {

    private String username;
    private String password1;
    private String password2;

    @Builder
    public MemberEditDto(String username, String password1, String password2) {
        this.username = username;
        this.password1 = password1;
        this.password2 = password2;
    }
    
    public boolean isPasswordSame() {
        if (password1 == null || password2 == null) return false;
        return password1.equals(password2);
    }
}
