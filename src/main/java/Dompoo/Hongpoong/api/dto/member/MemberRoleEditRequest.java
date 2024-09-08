package Dompoo.Hongpoong.api.dto.member;

import Dompoo.Hongpoong.domain.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
/*
RequestBody
{
    "role": "ROLE_LEADER"
}
 */
public class MemberRoleEditRequest {

    private Role role;

    @Builder
    public MemberRoleEditRequest(Role role) {
        this.role = role;
    }
}
