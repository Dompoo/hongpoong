package Dompoo.Hongpoong.domain.domain;

import Dompoo.Hongpoong.domain.enums.Club;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SignUp {

    private final Long id;
    private final String email;
    private final String name;
    private final String nickname;
    private final String password;
    private final Club club;
    private final Integer enrollmentNumber;
}
