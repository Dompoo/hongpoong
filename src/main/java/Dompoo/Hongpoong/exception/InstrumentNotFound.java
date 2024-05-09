package Dompoo.Hongpoong.exception;

public class InstrumentNotFound extends MyException {

    private static final String MESSAGE = "존재하지 않는 악기입니다.";
    private static final String STATUS_CODE = "404";

    public InstrumentNotFound() {
        super(MESSAGE);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}
