package Dompoo.Hongpoong.exception;

public class PasswordNotSame extends MyException {

    private static final String MESSAGE = "비밀번호와 비밀번호 확인이 일치하지 않습니다.";
    private static final String STATUS_CODE = "400";

    public PasswordNotSame() {
        super(MESSAGE);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}
