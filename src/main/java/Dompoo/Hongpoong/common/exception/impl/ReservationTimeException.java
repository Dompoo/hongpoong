package Dompoo.Hongpoong.common.exception.impl;

import Dompoo.Hongpoong.common.exception.config.MyException;

public class ReservationTimeException extends MyException {

    private static final String MESSAGE = "예약 불가능한 시간입니다.";
    private static final String STATUS_CODE = "400";

    public ReservationTimeException() {
        super(MESSAGE);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}
