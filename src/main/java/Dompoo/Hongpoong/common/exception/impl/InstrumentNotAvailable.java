package Dompoo.Hongpoong.common.exception.impl;

import Dompoo.Hongpoong.common.exception.config.MyException;

public class InstrumentNotAvailable extends MyException {

    private static final String MESSAGE = "빌릴 수 없는 악기입니다.";
    private static final String STATUS_CODE = "404";

    public InstrumentNotAvailable() {
        super(MESSAGE);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}
