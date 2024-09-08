package Dompoo.Hongpoong.api.dto.member;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberEditDto {

    private String name;
    private String password;
    
    @Builder
    private MemberEditDto(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
