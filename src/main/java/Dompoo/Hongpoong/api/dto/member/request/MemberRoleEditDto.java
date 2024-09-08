package Dompoo.Hongpoong.api.dto.member.request;

import Dompoo.Hongpoong.domain.enums.Role;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberRoleEditDto {

    private final Role role;
}
