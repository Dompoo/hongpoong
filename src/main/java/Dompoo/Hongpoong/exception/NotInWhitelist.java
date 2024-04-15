package Dompoo.Hongpoong.exception;

public class NotInWhitelist extends MyException {

    private static final String MESSAGE = "요청하지 않은 유저입니다.";
    private static final String STATUS_CODE = "400";

    public NotInWhitelist() {
        super(MESSAGE);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}
