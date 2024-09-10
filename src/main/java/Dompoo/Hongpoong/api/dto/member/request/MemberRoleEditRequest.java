package Dompoo.Hongpoong.api.dto.member.request;

import Dompoo.Hongpoong.domain.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
public class MemberRoleEditRequest {

    @NotNull(message = "역할은 비어있을 수 없습니다.")
    @Schema(enumAsRef = true)
    private final Role role;
    
    public MemberRoleEditDto toDto() {
        return MemberRoleEditDto.builder()
                .role(role)
                .build();
    }
}
