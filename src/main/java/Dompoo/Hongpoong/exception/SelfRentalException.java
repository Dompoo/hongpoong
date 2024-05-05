package Dompoo.Hongpoong.exception;

public class SelfRentalException extends MyException {

    private static final String MESSAGE = "자신에게 대여할 수 없습니다.";
    private static final String STATUS_CODE = "400";

    public SelfRentalException() {
        super(MESSAGE);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}
