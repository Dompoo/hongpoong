package Dompoo.Hongpoong.common.exception.impl;

import Dompoo.Hongpoong.common.exception.config.MyException;

public class RentalNotFound extends MyException {

    private static final String MESSAGE = "존재하지 않는 대여입니다.";
    private static final String STATUS_CODE = "404";

    public RentalNotFound() {
        super(MESSAGE);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}
