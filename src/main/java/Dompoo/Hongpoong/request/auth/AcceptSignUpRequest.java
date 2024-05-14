package Dompoo.Hongpoong.request.auth;

import jakarta.validation.constraints.Min;
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
    "emailId": 1,
    "acceptResult" : true
}
 */
public class AcceptSignUpRequest {

    @Min(value = 1, message = "승인/거절할 이메일을 선택해주세요.")
    private Long emailId;

    private boolean acceptResult;

    @Builder
    public AcceptSignUpRequest(Long emailId, boolean acceptResult) {
        this.emailId = emailId;
        this.acceptResult = acceptResult;
    }
}
