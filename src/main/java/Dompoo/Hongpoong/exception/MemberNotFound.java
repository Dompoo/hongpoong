package Dompoo.Hongpoong.exception;

public class MemberNotFound extends MyException {

    private static final String MESSAGE = "존재하지 않는 유저입니다.";
    private static final String STATUS_CODE = "404";

    public MemberNotFound() {
        super(MESSAGE);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}
