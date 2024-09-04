package Dompoo.Hongpoong.common.exception.impl;

import Dompoo.Hongpoong.common.exception.config.MyException;

public class AccessDeniedException extends MyException {

    private static final String MESSAGE = "접근할 수 없습니다.";
    private static final String STATUS_CODE = "403";

    public AccessDeniedException() {
        super(MESSAGE);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}