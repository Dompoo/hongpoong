package Dompoo.Hongpoong.api.controller;

import Dompoo.Hongpoong.api.dto.request.chat.ChatRoomCreateRequest;
import Dompoo.Hongpoong.api.dto.response.chat.ChatMessageDto;
import Dompoo.Hongpoong.api.dto.response.chat.ChatRoomResponse;
import Dompoo.Hongpoong.common.security.UserPrincipal;
import Dompoo.Hongpoong.api.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatService service;

    @PostMapping("")
    public ChatRoomResponse createRoom(@RequestBody ChatRoomCreateRequest request) {
        return service.createRoom(request);
    }

    @GetMapping("")
    public List<ChatRoomResponse> findAllRoom(@AuthenticationPrincipal UserPrincipal principal) {
        return service.findAllRoom(principal.getMemberId());
    }

    @DeleteMapping("/{roomId}")
    public void exitRoom(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long roomId){
        service.exitRoom(principal.getMemberId(), roomId);
    }

    @MessageMapping("/{roomId}") //여기로 전송되면 메서드 호출 -> WebSocketConfig prefixes 에서 적용한건 앞에 생략
    @SendTo("/room/{roomId}")   //구독하고 있는 장소로 메시지 전송 (목적지)  -> WebSocketConfig Broker 에서 적용한건 앞에 붙어줘야됨
    public ChatMessageDto chat(@DestinationVariable Long roomId, ChatMessageDto request) {
        //메시지 저장후 반환
        return service.createChat(roomId, request);
    }
}
