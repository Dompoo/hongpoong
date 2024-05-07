package Dompoo.Hongpoong.response.auth;

import Dompoo.Hongpoong.domain.Whitelist;
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
public class EmailResponse {

    private final Long id;
    private final String email;
    private final boolean isAccepted;

    @Builder
    public EmailResponse(Whitelist whitelist) {
        this.id = whitelist.getId();
        this.email = whitelist.getEmail();
        this.isAccepted = whitelist.getIsAccepted();
    }
}
