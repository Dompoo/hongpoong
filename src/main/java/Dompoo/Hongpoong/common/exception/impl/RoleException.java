package Dompoo.Hongpoong.common.exception.impl;

import Dompoo.Hongpoong.common.exception.config.MyException;

public class RoleException extends MyException {

    private static final String MESSAGE = "해당 역할이 존재하지 않습니다.";
    private static final String STATUS_CODE = "400";

    public RoleException() {
        super(MESSAGE);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}
