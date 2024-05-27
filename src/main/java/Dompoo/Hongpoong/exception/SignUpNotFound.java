package Dompoo.Hongpoong.exception;

import Dompoo.Hongpoong.exception.config.MyException;

public class SignUpNotFound extends MyException {

    private static final String MESSAGE = "존재하지 않는 회원가입 요청입니다.";
    private static final String STATUS_CODE = "404";

    public SignUpNotFound() {
        super(MESSAGE);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}
