package Dompoo.Hongpoong.exception;

public class AlreadyAcceptedEmail extends MyException {

    private static final String MESSAGE = "이미 승인된 이메일입니다.";
    private static final String STATUS_CODE = "400";

    public AlreadyAcceptedEmail() {
        super(MESSAGE);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}
