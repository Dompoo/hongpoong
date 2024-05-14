package Dompoo.Hongpoong.response.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/*
ResponseBody
{
    "valid": true
}
 */
public class EmailValidResponse {

    private Boolean valid;

    @Builder
    public EmailValidResponse(Boolean valid) {
        this.valid = valid;
    }
}
