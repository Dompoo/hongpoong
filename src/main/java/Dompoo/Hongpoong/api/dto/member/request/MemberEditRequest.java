package Dompoo.Hongpoong.api.dto.member.request;

import Dompoo.Hongpoong.domain.enums.Club;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
public class MemberEditRequest {
    
    @NotBlank(message = "비밀번호는 비어있을 수 없습니다.")
    @Schema(example = "1234")
    private final String password;
    
    @Schema(example = "이창근")
    private final String name;
    
    @Schema(example = "불꽃남자")
    private final String nickname;
    
    @Schema(enumAsRef = true)
    private final Club club;
    
    @Schema(example = "19")
    private final Integer enrollmentNumber;
    
    @Schema(example = "image.com/1")
    private final String profileImageUrl;
    
    @Schema(example = "5678")
    private final String newPassword;
    
    public MemberEditDto toDto() {
        return MemberEditDto.builder()
                .name(name)
                .nickname(nickname)
                .club(club)
                .enrollmentNumber(enrollmentNumber)
                .profileImageUrl(profileImageUrl)
                .newPassword(newPassword)
                .build();
    }
}
