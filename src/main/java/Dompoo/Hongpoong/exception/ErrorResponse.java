package Dompoo.Hongpoong.exception;

import lombok.*;

@Getter
@Setter
public class ErrorResponse {

    private final String code;
    private final String message;

    @Builder
    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
