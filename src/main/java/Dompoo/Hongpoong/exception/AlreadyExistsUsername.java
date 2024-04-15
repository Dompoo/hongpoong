package Dompoo.Hongpoong.exception;

public class AlreadyExistsUsername extends MyException {

    private static final String MESSAGE = "이미 존재하는 유저명입니다.";
    private static final String STATUS_CODE = "400";

    public AlreadyExistsUsername() {
        super(MESSAGE);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}
