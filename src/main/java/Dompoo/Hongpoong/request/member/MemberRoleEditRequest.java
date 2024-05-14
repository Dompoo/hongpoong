package Dompoo.Hongpoong.request.member;

import Dompoo.Hongpoong.domain.Member;
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

    private Member.Role role;

    @Builder
    public MemberRoleEditRequest(Member.Role role) {
        this.role = role;
    }
}
