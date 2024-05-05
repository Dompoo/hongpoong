package Dompoo.Hongpoong.exception;

public class AlreadyUsedEmail extends MyException {

    private static final String MESSAGE = "이미 회원가입된 이메일입니다.";
    private static final String STATUS_CODE = "400";

    public AlreadyUsedEmail() {
        super(MESSAGE);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}
