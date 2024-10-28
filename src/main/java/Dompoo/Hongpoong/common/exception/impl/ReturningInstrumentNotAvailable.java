package Dompoo.Hongpoong.common.exception.impl;

import Dompoo.Hongpoong.common.exception.config.MyException;

public class ReturningInstrumentNotAvailable extends MyException {

    private static final String MESSAGE = "반납할 수 없는 악기입니다.";
    private static final String STATUS_CODE = "400";

    public ReturningInstrumentNotAvailable() {
        super(MESSAGE);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}
