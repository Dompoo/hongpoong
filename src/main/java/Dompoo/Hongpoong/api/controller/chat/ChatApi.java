package Dompoo.Hongpoong.api.controller.chat;

import Dompoo.Hongpoong.api.dto.chat.request.ChatMessageRequest;
import Dompoo.Hongpoong.api.dto.chat.request.ChatRoomCreateRequest;
import Dompoo.Hongpoong.api.dto.chat.response.ChatMessageResponse;
import Dompoo.Hongpoong.api.dto.chat.response.ChatRoomResponse;
import Dompoo.Hongpoong.common.security.UserClaims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "5. 채팅")
public interface ChatApi {
	
	@Operation(summary = "채팅방 생성")
	ChatRoomResponse createRoom(
			@RequestBody ChatRoomCreateRequest request
	);
	
	@Operation(summary = "내가 속한 전체 채팅방 조회")
	List<ChatRoomResponse> findAllRoom(
			@Schema(hidden = true) UserClaims claims
	);
	
	@Operation(summary = "채팅방 나가기")
	void exitRoom(
			@Schema(hidden = true) UserClaims claims,
			@Parameter(description = "채팅방 id") Long roomId
	);
	
	@Operation(summary = "채팅하기")
	ChatMessageResponse chat(
			@Parameter(description = "채팅방 id") Long roomId,
			@RequestBody ChatMessageRequest request
	);
}
