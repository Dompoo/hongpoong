package Dompoo.Hongpoong.common.exception.impl;

import Dompoo.Hongpoong.common.exception.config.MyException;

public class AlreadyExistEmail extends MyException {

    private static final String MESSAGE = "이미 존재하는 이메일입니다.";
    private static final String STATUS_CODE = "400";

    public AlreadyExistEmail() {
        super(MESSAGE);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}
