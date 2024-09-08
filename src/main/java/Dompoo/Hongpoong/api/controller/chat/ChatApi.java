package Dompoo.Hongpoong.api.controller.chat;

import Dompoo.Hongpoong.api.dto.chat.ChatMessageDto;
import Dompoo.Hongpoong.api.dto.chat.ChatRoomCreateRequest;
import Dompoo.Hongpoong.api.dto.chat.ChatRoomResponse;
import Dompoo.Hongpoong.common.security.UserClaims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "5. 채팅")
public interface ChatApi {
	
	@Operation(summary = "채팅방 생성")
	ChatRoomResponse createRoom(ChatRoomCreateRequest request);
	
	@Operation(summary = "내가 속한 전체 채팅방 조회")
	List<ChatRoomResponse> findAllRoom(UserClaims claims);
	
	@Operation(summary = "채팅방 나가기")
	void exitRoom(UserClaims claims, Long roomId);
	
	@Operation(summary = "채팅하기")
	ChatMessageDto chat(Long roomId, ChatMessageDto request);
}
