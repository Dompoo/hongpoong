package Dompoo.Hongpoong.common.exception.impl;

import Dompoo.Hongpoong.common.exception.config.MyException;

public class TimeExtendNotAvailableException extends MyException {

    private static final String MESSAGE = "시간 연장은 연습 종료 30분 전에만 가능합니다.";
    private static final String STATUS_CODE = "400";

    public TimeExtendNotAvailableException() {
        super(MESSAGE);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}
