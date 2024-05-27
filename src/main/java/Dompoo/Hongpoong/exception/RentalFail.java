package Dompoo.Hongpoong.exception;

import Dompoo.Hongpoong.exception.config.MyException;

public class RentalFail extends MyException {

    private static final String MESSAGE = "대여할 수 없습니다.";
    private static final String STATUS_CODE = "400";

    public RentalFail() {
        super(MESSAGE);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}
