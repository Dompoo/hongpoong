package Dompoo.Hongpoong.response.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
/*
ResponseBody
{
    "valid": true
}
 */
public class EmailValidResponse {

    private final Boolean valid;

    @Builder
    public EmailValidResponse(Boolean valid) {
        this.valid = valid;
    }
}
