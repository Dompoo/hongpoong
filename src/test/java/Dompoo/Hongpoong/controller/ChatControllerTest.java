package Dompoo.Hongpoong.controller;

import Dompoo.Hongpoong.api.dto.request.chat.ChatRoomCreateRequest;
import Dompoo.Hongpoong.config.WithMockMember;
import Dompoo.Hongpoong.domain.ChatRoom;
import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.repository.ChatMessageRepository;
import Dompoo.Hongpoong.repository.ChatRoomRepository;
import Dompoo.Hongpoong.repository.MemberRepository;
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
    private ChatRoomRepository roomRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
	@Autowired
	private ChatRoomRepository chatRoomRepository;
    
    @AfterEach
    void setUp() {
        messageRepository.deleteAll();
        roomRepository.deleteAll();
        memberRepository.deleteAll();
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
                .club(Member.Club.SANTLE)
                .build());
        Member member3 = memberRepository.save(Member.builder()
                .email("user3@gmail.com")
                .username("user3")
                .password("1234")
                .club(Member.Club.SANTLE)
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
                .club(Member.Club.SANTLE)
                .build());
        Member member3 = memberRepository.save(Member.builder()
                .email("user3@gmail.com")
                .username("user3")
                .password("1234")
                .club(Member.Club.SANTLE)
                .build());

        ChatRoom room1 = ChatRoom.builder()
                .roomName("채팅방1")
                .build();
        room1.addMember(me);
        room1.addMember(member2);

        ChatRoom room2 = ChatRoom.builder()
                .roomName("채팅방2")
                .build();
        room2.addMember(member2);
        room2.addMember(member3);
        
        chatRoomRepository.saveAll(List.of(room1, room2));

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
                .club(Member.Club.SANTLE)
                .build());
        Member member3 = memberRepository.save(Member.builder()
                .email("user3@gmail.com")
                .username("user3")
                .password("1234")
                .club(Member.Club.SANTLE)
                .build());

        ChatRoom room = ChatRoom.builder()
                .roomName("채팅방1")
                .build();
        room.addMember(me);
        room.addMember(member2);
        room.addMember(member3);
        roomRepository.save(room);

        //expected
        mockMvc.perform(delete("/chat/{roomId}", room.getId()))
                .andExpect(status().isOk())
                .andDo(print());
    }
    
}