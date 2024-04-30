package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.domain.ChatRoom;
import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.exception.ChatFailException;
import Dompoo.Hongpoong.exception.ChatRoomNotFound;
import Dompoo.Hongpoong.request.chat.ChatMessageCreateRequest;
import Dompoo.Hongpoong.repository.ChatRoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
@Data
public class SocketService {

    private final ObjectMapper mapper;

    //roomId : session
    private final Map<Long, Set<WebSocketSession>> chatSessions = new HashMap<>();
    private final ChatRoomRepository repository;

    public void handleAction(WebSocketSession session, ChatMessageCreateRequest message) {
        ChatRoom chatRoom = repository.findById(message.getRoomId())
                .orElseThrow(ChatRoomNotFound::new);

        //채팅방에 속해있지 않은 경우
        if (!chatRoom.getMembers().stream().map(Member::getUsername).toList().contains(message.getSender())) {
            throw new ChatFailException();
        }

        //접속한 경우
        if (message.getType().equals(ChatMessageCreateRequest.MessageType.ENTER)) {
            // chatSessions 에 넘어온 session 을 담고,
            Set<WebSocketSession> sessions = chatSessions.getOrDefault(message.getRoomId(), new HashSet<>());
            sessions.add(session);
            chatSessions.put(message.getRoomId(), sessions);

            // message 에는 입장하였다는 메시지를 띄운다
            message.setMessage(message.getSender() + " 님이 입장하셨습니다");
            sendMessageToRoom(message.getRoomId(), message);
        }

        //메시지를 보낸 경우
        else if (message.getType().equals(ChatMessageCreateRequest.MessageType.TALK)) {
            message.setMessage(message.getMessage());
            sendMessageToRoom(message.getRoomId(), message);
        }
    }

    public void sendMessageToRoom(Long roomId, ChatMessageCreateRequest message) {
        chatSessions.get(roomId).parallelStream().forEach(session -> sendMessage(session, message));
    }

    private void sendMessage(WebSocketSession session, ChatMessageCreateRequest message) {
        try{
            session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
