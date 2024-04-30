package Dompoo.Hongpoong.exception;

public class ChatFailException extends MyException {

    private static final String MESSAGE = "채팅할 수 없습니다.";
    private static final String STATUS_CODE = "403";

    public ChatFailException() {
        super(MESSAGE);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}
