package Dompoo.Hongpoong.api.dto.member.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberEditDto {

    private final String name;
    private final String password;
}
