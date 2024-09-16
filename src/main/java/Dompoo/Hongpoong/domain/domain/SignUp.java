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

    private Long id;
    private String email;
    private String name;
    private String nickname;
    private String password;
    private Club club;
    private Integer enrollmentNumber;
}
