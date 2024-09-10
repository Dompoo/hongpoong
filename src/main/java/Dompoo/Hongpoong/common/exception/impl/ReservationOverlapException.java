package Dompoo.Hongpoong.common.exception.impl;

import Dompoo.Hongpoong.common.exception.config.MyException;

public class ReservationOverlapException extends MyException {

    private static final String MESSAGE = "이미 다른 예약이 존재합니다.";
    private static final String STATUS_CODE = "400";

    public ReservationOverlapException() {
        super(MESSAGE);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}
