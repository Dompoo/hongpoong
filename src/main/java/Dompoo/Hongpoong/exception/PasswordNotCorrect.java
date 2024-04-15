package Dompoo.Hongpoong.exception;

public class PasswordNotCorrect extends MyException {

    private static final String MESSAGE = "비밀번호가 일치하지 않습니다.";
    private static final String STATUS_CODE = "400";

    public PasswordNotCorrect() {
        super(MESSAGE);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}
