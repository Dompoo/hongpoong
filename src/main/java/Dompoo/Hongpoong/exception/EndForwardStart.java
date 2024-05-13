package Dompoo.Hongpoong.exception;

public class EndForwardStart extends MyException {

    private static final String MESSAGE = "시작 시간은 종료 시간보다 앞서야 합니다.";
    private static final String STATUS_CODE = "400";

    public EndForwardStart() {
        super(MESSAGE);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}
