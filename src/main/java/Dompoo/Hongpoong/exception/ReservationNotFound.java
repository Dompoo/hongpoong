package Dompoo.Hongpoong.exception;

public class ReservationNotFound extends MyException {

    private static final String MESSAGE = "존재하지 않는 예약입니다.";
    private static final String STATUS_CODE = "404";

    public ReservationNotFound() {
        super(MESSAGE);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}
