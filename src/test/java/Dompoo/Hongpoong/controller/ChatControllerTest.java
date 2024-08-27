package Dompoo.Hongpoong.controller;

import Dompoo.Hongpoong.api.dto.request.chat.ChatRoomCreateRequest;
import Dompoo.Hongpoong.config.WithMockMember;
import Dompoo.Hongpoong.domain.entity.ChatRoom;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.entity.MemberInChatRoom;
import Dompoo.Hongpoong.domain.enums.Club;
import Dompoo.Hongpoong.domain.repository.ChatMessageRepository;
import Dompoo.Hongpoong.domain.repository.ChatRoomRepository;
import Dompoo.Hongpoong.domain.repository.MemberInChatRoomRepository;
import Dompoo.Hongpoong.domain.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class ChatControllerTest {

    @Autowired
    private ChatMessageRepository messageRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
	@Autowired
	private ChatRoomRepository chatRoomRepository;
	@Autowired
	private MemberInChatRoomRepository memberInChatRoomRepository;
    
    @AfterEach
    void setUp() {
        messageRepository.deleteAllInBatch();
        memberInChatRoomRepository.deleteAllInBatch();
        chatRoomRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("채팅방 생성")
    @WithMockMember
    void createRoom() throws Exception {
        //given
        Member me = memberRepository.findAll().getLast();
        Member member2 = memberRepository.save(Member.builder()
                .email("user2@gmail.com")
                .username("user2")
                .password("1234")
                .club(Club.SANTLE)
                .build());
        Member member3 = memberRepository.save(Member.builder()
                .email("user3@gmail.com")
                .username("user3")
                .password("1234")
                .club(Club.SANTLE)
                .build());

        ChatRoomCreateRequest request = ChatRoomCreateRequest.builder()
                .members(List.of(me.getId(), member2.getId(), member3.getId()))
                .name("채팅방1")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomName").value("채팅방1"))
                .andExpect(jsonPath("$.memberCount").value(3))
                .andDo(print());
    }

    @Test
    @DisplayName("채팅방 불러오기")
    @WithMockMember
    void getChatRooms() throws Exception {
        //given
        Member me = memberRepository.findAll().getLast();
        Member member2 = memberRepository.save(Member.builder()
                .email("user2@gmail.com")
                .username("user2")
                .password("1234")
                .club(Club.SANTLE)
                .build());
        Member member3 = memberRepository.save(Member.builder()
                .email("user3@gmail.com")
                .username("user3")
                .password("1234")
                .club(Club.SANTLE)
                .build());
        
        ChatRoom chatroom1 = chatRoomRepository.save(ChatRoom.builder()
                .roomName("채팅방1")
                .memberCount(2)
                .build());
        ChatRoom chatroom2 = chatRoomRepository.save(ChatRoom.builder()
                .roomName("채팅방2")
                .memberCount(2)
                .build());
        
        memberInChatRoomRepository.saveAll(List.of(
                MemberInChatRoom.builder()
                        .chatRoom(chatroom1)
                        .member(me)
                        .build(),
                MemberInChatRoom.builder()
                        .chatRoom(chatroom1)
                        .member(member2)
                        .build(),
                MemberInChatRoom.builder()
                        .chatRoom(chatroom2)
                        .member(member2)
                        .build(),
                MemberInChatRoom.builder()
                        .chatRoom(chatroom2)
                        .member(member3)
                        .build()
        ));

        //expected
        mockMvc.perform(get("/chat"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].roomName").value("채팅방1"))
                .andExpect(jsonPath("$[0].memberCount").value(2))
                .andDo(print());
    }

    @Test
    @DisplayName("채팅방 삭제")
    @WithMockMember
    void deleteChatRoom() throws Exception {
        //given
        Member me = memberRepository.findAll().getLast();
        Member member2 = memberRepository.save(Member.builder()
                .email("user2@gmail.com")
                .username("user2")
                .password("1234")
                .club(Club.SANTLE)
                .build());
        Member member3 = memberRepository.save(Member.builder()
                .email("user3@gmail.com")
                .username("user3")
                .password("1234")
                .club(Club.SANTLE)
                .build());

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.builder()
                .roomName("채팅방1")
                .build());
        
        memberInChatRoomRepository.saveAll(List.of(
                MemberInChatRoom.builder()
                        .chatRoom(chatRoom)
                        .member(me)
                        .build(),
                MemberInChatRoom.builder()
                        .chatRoom(chatRoom)
                        .member(member2)
                        .build(),
                MemberInChatRoom.builder()
                        .chatRoom(chatRoom)
                        .member(member3)
                        .build()
        ));
        

        //expected
        mockMvc.perform(delete("/chat/{roomId}", chatRoom.getId()))
                .andExpect(status().isOk())
                .andDo(print());
    }
    
}