package Dompoo.Hongpoong.common.exception.impl;

import Dompoo.Hongpoong.common.exception.config.MyException;

public class EditRoleToAdminException extends MyException {

    private static final String MESSAGE = "의장으로 권한을 변경할 수 없습니다.";
    private static final String STATUS_CODE = "400";

    public EditRoleToAdminException() {
        super(MESSAGE);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}
