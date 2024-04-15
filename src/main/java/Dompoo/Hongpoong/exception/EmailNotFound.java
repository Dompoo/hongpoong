package Dompoo.Hongpoong.exception;

public class EmailNotFound extends MyException {

    private static final String MESSAGE = "존재하지 않는 이메일입니다.";
    private static final String STATUS_CODE = "404";

    public EmailNotFound() {
        super(MESSAGE);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}
