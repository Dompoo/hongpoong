package Dompoo.Hongpoong.api.dto.member.request;

import Dompoo.Hongpoong.domain.enums.Club;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberEditDto {
    
    private final String name;
    private final String nickname;
    private final Club club;
    private final Integer enrollmentNumber;
    private final String profileImageUrl;
    private final String newPassword;
}
