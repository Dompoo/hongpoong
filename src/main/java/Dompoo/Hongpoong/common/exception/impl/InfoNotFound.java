package Dompoo.Hongpoong.common.exception.impl;

import Dompoo.Hongpoong.common.exception.config.MyException;

public class InfoNotFound extends MyException {

    private static final String MESSAGE = "존재하지 않는 공지사항입니다.";
    private static final String STATUS_CODE = "404";

    public InfoNotFound() {
        super(MESSAGE);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}
