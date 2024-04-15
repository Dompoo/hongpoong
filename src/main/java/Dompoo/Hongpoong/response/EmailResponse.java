package Dompoo.Hongpoong.response;

import Dompoo.Hongpoong.domain.Whitelist;
import lombok.Builder;
import lombok.Getter;

@Getter
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
