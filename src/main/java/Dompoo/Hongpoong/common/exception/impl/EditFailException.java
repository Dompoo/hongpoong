package Dompoo.Hongpoong.common.exception.impl;

import Dompoo.Hongpoong.common.exception.config.MyException;

public class EditFailException extends MyException {

    private static final String MESSAGE = "수정할 수 없습니다.";
    private static final String STATUS_CODE = "403";

    public EditFailException() {
        super(MESSAGE);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}
