package Dompoo.Hongpoong.controller;

import Dompoo.Hongpoong.api.dto.chat.ChatRoomCreateRequest;
import Dompoo.Hongpoong.api.dto.chat.response.ChatRoomResponse;
import Dompoo.Hongpoong.config.MyWebMvcTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ChatControllerTest extends MyWebMvcTest {
    
    private static final Long MEMBER_ID = 1L;
    private static final Long CHATROOM_ID = 1L;
    private static final int CHATROOM_MEMBER_COUNT = 4;
    private static final String CHATROOM_NAME = "20240517 분쟁해결";

    @Test
    @DisplayName("채팅방 생성")
    void createRoom() throws Exception {
        //given
        when(chatService.createRoom(any())).thenReturn(ChatRoomResponse.builder()
                .id(CHATROOM_ID)
                .roomName(CHATROOM_NAME)
                .memberCount(CHATROOM_MEMBER_COUNT)
                .build());
        
        ChatRoomCreateRequest request = ChatRoomCreateRequest.builder()
                .members(List.of(MEMBER_ID))
                .name(CHATROOM_NAME)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CHATROOM_ID))
                .andExpect(jsonPath("$.roomName").value(CHATROOM_NAME))
                .andExpect(jsonPath("$.memberCount").value(CHATROOM_MEMBER_COUNT))
                .andDo(print());
    }

    @Test
    @DisplayName("채팅방 불러오기")
    void getChatRooms() throws Exception {
        //given
        when(chatService.findAllRoom(any())).thenReturn(List.of(
                ChatRoomResponse.builder()
                        .id(CHATROOM_ID)
                        .roomName(CHATROOM_NAME)
                        .memberCount(CHATROOM_MEMBER_COUNT)
                        .build()
        ));

        //expected
        mockMvc.perform(get("/chat"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(CHATROOM_ID))
                .andExpect(jsonPath("$[0].roomName").value(CHATROOM_NAME))
                .andExpect(jsonPath("$[0].memberCount").value(CHATROOM_MEMBER_COUNT))
                .andDo(print());
    }

    @Test
    @DisplayName("채팅방 삭제")
    void deleteChatRoom() throws Exception {
        //given

        //expected
        mockMvc.perform(delete("/chat/{roomId}", CHATROOM_ID))
                .andExpect(status().isOk())
                .andDo(print());
    }
    
}