package Dompoo.Hongpoong.exception;

public class ReturnFail extends MyException {

    private static final String MESSAGE = "반납할 수 없습니다.";
    private static final String STATUS_CODE = "400";

    public ReturnFail() {
        super(MESSAGE);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}
