package Dompoo.Hongpoong.request.member;

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
    "memberRole": "USER"
}
 */
public class MemberRoleEditRequest {

    private boolean isAdmin;

    @Builder
    public MemberRoleEditRequest(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
