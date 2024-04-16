package Dompoo.Hongpoong.exception;

public class AlreadyExistsEmail extends MyException {

    private static final String MESSAGE = "이미 존재하는 이메일입니다.";
    private static final String STATUS_CODE = "400";

    public AlreadyExistsEmail() {
        super(MESSAGE);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}
