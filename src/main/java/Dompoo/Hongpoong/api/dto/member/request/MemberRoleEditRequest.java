package Dompoo.Hongpoong.api.dto.member.request;

import Dompoo.Hongpoong.domain.enums.Role;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
public class MemberRoleEditRequest {

    private final Role role;
}
