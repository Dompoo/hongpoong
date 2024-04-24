package Dompoo.Hongpoong.exception;

public class DeleteFailException extends MyException {

    private static final String MESSAGE = "삭제할 수 없습니다.";
    private static final String STATUS_CODE = "403";

    public DeleteFailException() {
        super(MESSAGE);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}
