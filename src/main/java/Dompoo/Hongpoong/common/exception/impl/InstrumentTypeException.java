package Dompoo.Hongpoong.common.exception.impl;

import Dompoo.Hongpoong.common.exception.config.MyException;

public class InstrumentTypeException extends MyException {

    private static final String MESSAGE = "입력 불가능한 악기입니다.";
    private static final String STATUS_CODE = "400";

    public InstrumentTypeException() {
        super(MESSAGE);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}
