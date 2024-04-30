package Dompoo.Hongpoong.exception;

public class ChatRoomNotFound extends MyException {

    private static final String MESSAGE = "존재하지 않는 채팅방입니다.";
    private static final String STATUS_CODE = "404";

    public ChatRoomNotFound() {
        super(MESSAGE);
    }

    @Override
    public String statusCode() {
        return STATUS_CODE;
    }
}
