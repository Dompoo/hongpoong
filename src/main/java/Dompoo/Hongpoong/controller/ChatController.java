package Dompoo.Hongpoong.controller;

import Dompoo.Hongpoong.request.chat.ChatRoomCreateRequest;
import Dompoo.Hongpoong.response.ChatRoomResponse;
import Dompoo.Hongpoong.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatService service;

    @PostMapping
    public ChatRoomResponse createRoom(@RequestBody ChatRoomCreateRequest request) {
        return service.createRoom(request);
    }

    @GetMapping
    public List<ChatRoomResponse> findAllRoom() {
        return service.findAll();
    }

//    @GetMapping("/{roomId}")
//    public ChatRoomDetailResponse findOneRoom(@PathVariable Long roomId) {
//        return service.findOne(roomId);
//    }
}
