package Dompoo.Hongpoong.common.exception.impl;

import Dompoo.Hongpoong.common.exception.config.MyException;

public class LoginFailException extends MyException {

    private static final String MESSAGE = "이메일 또는 비밀번호가 잘못되었습니다.";
    private static final String STATUS_CODE = "400";

    public LoginFailException() {
        super(MESSAGE);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}
