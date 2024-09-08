package Dompoo.Hongpoong.api.dto.member.response;

import Dompoo.Hongpoong.domain.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberStatusResponse {
    
    @Schema(example = "1")
    private final Long id;
    
    @Schema(example = "email@gmail.com")
    private final String email;
    
    @Schema(example = "이창근")
    private final String username;
    
    @Schema(example = "1234")
    private final String password;
    
    @Schema(example = "산틀")
    private final String club;
    
    public static MemberStatusResponse from(Member member) {
        return MemberStatusResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .username(member.getName())
                .password(member.getPassword())
                .club(member.getClub().korName)
                .build();
    }
}
