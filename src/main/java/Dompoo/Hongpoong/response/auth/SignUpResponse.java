package Dompoo.Hongpoong.response.auth;

import Dompoo.Hongpoong.domain.SignUp;
import lombok.Builder;
import lombok.Getter;

@Getter
/*
ResponseBody

<List조회시>
[
    {
        "id" : 1,
        "email" : "dompoo@gmail.com",
        "isAccepted" : true
    },
    {
        "id" : 2,
        "email" : "dompoo@gmail.com",
        "isAccepted" : true
    },
    . . .
]
 */
public class SignUpResponse {

    private final Long id;
    private final String email;
    private final boolean isAccepted;

    @Builder
    public SignUpResponse(SignUp signUp) {
        this.id = signUp.getId();
        this.email = signUp.getEmail();
        this.isAccepted = signUp.getIsAccepted();
    }
}
