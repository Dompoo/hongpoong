package Dompoo.Hongpoong.common.exception.impl;

import Dompoo.Hongpoong.common.exception.config.MyException;

public class AttendanceNotFound extends MyException {

    private static final String MESSAGE = "출석현황을 찾을 수 없습니다.";
    private static final String STATUS_CODE = "404";

    public AttendanceNotFound() {
        super(MESSAGE);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}
