package Dompoo.Hongpoong.common.exception.impl;

import Dompoo.Hongpoong.common.exception.config.MyException;

public class LoginExpiredException extends MyException {

    private static final String MESSAGE = "다시 로그인 해주세요.";
    private static final String STATUS_CODE = "400";

    public LoginExpiredException() {
        super(MESSAGE);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}