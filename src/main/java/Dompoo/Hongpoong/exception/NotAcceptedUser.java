package Dompoo.Hongpoong.exception;

public class NotAcceptedUser extends MyException {

    private static final String MESSAGE = "승인되지 않은 유저입니다.";
    private static final String STATUS_CODE = "400";

    public NotAcceptedUser() {
        super(MESSAGE);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}
