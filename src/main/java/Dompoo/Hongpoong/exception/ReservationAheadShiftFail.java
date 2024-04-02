package Dompoo.Hongpoong.exception;

public class ReservationAheadShiftFail extends MyException {

    private static final String MESSAGE = "현재 우선순위보다 높습니다.";
    private static final String STATUS_CODE = "400";

    public ReservationAheadShiftFail() {
        super(MESSAGE);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}
