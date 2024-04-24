package Dompoo.Hongpoong.controller;

import Dompoo.Hongpoong.config.WithMockMember;
import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.domain.Rental;
import Dompoo.Hongpoong.repository.MemberRepository;
import Dompoo.Hongpoong.repository.RentalRepository;
import Dompoo.Hongpoong.request.rental.RentalCreateRequest;
import Dompoo.Hongpoong.request.rental.RentalEditRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class RentalControllerTest {

    @Autowired
    private RentalRepository rentalRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static final String RESPONSE_MEMBER_USERNAME = "창근";
    private static final String RESPONSE_MEMBER_EMAIL = "dompoo@gmail.com";
    private static final String RESPONSE_MEMBER_PASSWORD = "1234";
    private static final String REQUEST_MEMBER_USERNAME = "윤호";
    private static final String REQUEST_MEMBER_EMAIL = "yoonH@naver.com";
    private static final String REQUEST_MEMBER_PASSWORD = "qwer";
    private static final String RENTAL_PRODUCT = "장구";
    private static final int RENTAL_COUNT = 1;
    private static final LocalDate RENTAL_DATE = LocalDate.of(2025, 12, 20);
    private static final String RENTAL_DATE_STRING = "2025-12-20";
    private static final int RENTAL_TIME = 13;

    @BeforeEach
    void setUp() {
        rentalRepository.deleteAll();
    }

    /**
     * 대여 전체 조회 API 테스트 코드
     */

    @Test
    @DisplayName("전체 대여 조회")
    @WithMockMember
    void list() throws Exception {
        //given
        Member member1 = memberRepository.save(Member.builder()
                .email(REQUEST_MEMBER_EMAIL)
                .username(REQUEST_MEMBER_USERNAME)
                .password(REQUEST_MEMBER_PASSWORD)
                .build());

        Member member2 = memberRepository.save(Member.builder()
                .email(RESPONSE_MEMBER_EMAIL)
                .username(RESPONSE_MEMBER_USERNAME)
                .password(RESPONSE_MEMBER_PASSWORD)
                .build());

        rentalRepository.save(Rental.builder()
                .product(RENTAL_PRODUCT)
                .count(RENTAL_COUNT)
                .requestMember(member1)
                .responseMember(member2)
                .date(RENTAL_DATE)
                .time(RENTAL_TIME)
                .build());

        rentalRepository.save(Rental.builder()
                .product("소고")
                .count(4)
                .requestMember(member2)
                .responseMember(member1)
                .date(LocalDate.of(2025, 12, 30))
                .time(17)
                .build());

        //expected
        mockMvc.perform(get("/rental"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].product").value(RENTAL_PRODUCT))
                .andExpect(jsonPath("$[0].count").value(RENTAL_COUNT))
                .andExpect(jsonPath("$[0].requestMember").value(REQUEST_MEMBER_USERNAME))
                .andExpect(jsonPath("$[0].responseMember").value(RESPONSE_MEMBER_USERNAME))
                .andExpect(jsonPath("$[0].date").value(RENTAL_DATE_STRING))
                .andExpect(jsonPath("$[0].time").value(RENTAL_TIME))
                .andExpect(jsonPath("$[1].product").value("소고"))
                .andExpect(jsonPath("$[1].count").value(4))
                .andExpect(jsonPath("$[1].requestMember").value(RESPONSE_MEMBER_USERNAME))
                .andExpect(jsonPath("$[1].responseMember").value(REQUEST_MEMBER_USERNAME))
                .andExpect(jsonPath("$[1].date").value("2025-12-30"))
                .andExpect(jsonPath("$[1].time").value(17))
                .andDo(print());
    }

    /**
     * 대여 추가 API 테스트 코드
     */

    @Test
    @DisplayName("대여 추가")
    @WithMockMember
    void addOne() throws Exception {
        //given
        memberRepository.save(Member.builder()
                .email(REQUEST_MEMBER_EMAIL)
                .username(REQUEST_MEMBER_USERNAME)
                .password(REQUEST_MEMBER_PASSWORD)
                .build());

        RentalCreateRequest request = RentalCreateRequest.builder()
                .product(RENTAL_PRODUCT)
                .count(RENTAL_COUNT)
                .responseMember(REQUEST_MEMBER_USERNAME)
                .date(RENTAL_DATE)
                .time(RENTAL_TIME)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/rental")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("대여 추가시 품목은 비어있으면 안된다.")
    @WithMockMember
    void addOneFailRENTAL_COUNT() throws Exception {
        //given
        memberRepository.save(Member.builder()
                .email(REQUEST_MEMBER_EMAIL)
                .username(REQUEST_MEMBER_USERNAME)
                .password(REQUEST_MEMBER_PASSWORD)
                .build());

        RentalCreateRequest request = RentalCreateRequest.builder()
                .count(RENTAL_COUNT)
                .responseMember(REQUEST_MEMBER_USERNAME)
                .date(RENTAL_DATE)
                .time(RENTAL_TIME)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/rental")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[품목은 비어있을 수 없습니다.]"))
                .andDo(print());
    }

    @Test
    @DisplayName("대여 추가시 품목은 공백으로 비어있으면 안된다.")
    @WithMockMember
    void addOneFail2() throws Exception {
        //given
        memberRepository.save(Member.builder()
                .email(REQUEST_MEMBER_EMAIL)
                .username(REQUEST_MEMBER_USERNAME)
                .password(REQUEST_MEMBER_PASSWORD)
                .build());

        RentalCreateRequest request = RentalCreateRequest.builder()
                .product("")
                .count(RENTAL_COUNT)
                .responseMember(REQUEST_MEMBER_USERNAME)
                .date(RENTAL_DATE)
                .time(RENTAL_TIME)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/rental")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[품목은 비어있을 수 없습니다.]"))
                .andDo(print());
    }

    @Test
    @DisplayName("대여 추가시 대여 개수는 RENTAL_COUNT개 이상이어야 한다.")
    @WithMockMember
    void addOneFail3() throws Exception {
        //given
        memberRepository.save(Member.builder()
                .email(REQUEST_MEMBER_EMAIL)
                .username(REQUEST_MEMBER_USERNAME)
                .password(REQUEST_MEMBER_PASSWORD)
                .build());

        RentalCreateRequest request = RentalCreateRequest.builder()
                .product(RENTAL_PRODUCT)
                .count(0)
                .responseMember(REQUEST_MEMBER_USERNAME)
                .date(RENTAL_DATE)
                .time(RENTAL_TIME)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/rental")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[개수는 1개 이상이어야 합니다.]"))
                .andDo(print());
    }

    @Test
    @DisplayName("대여 추가시 대상 멤버는 비어 있으면 안된다.")
    @WithMockMember
    void addOneFail4() throws Exception {
        //given
        memberRepository.save(Member.builder()
                .email(REQUEST_MEMBER_EMAIL)
                .username(REQUEST_MEMBER_USERNAME)
                .password(REQUEST_MEMBER_PASSWORD)
                .build());

        RentalCreateRequest request = RentalCreateRequest.builder()
                .product(RENTAL_PRODUCT)
                .count(RENTAL_COUNT)
                .date(RENTAL_DATE)
                .time(RENTAL_TIME)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/rental")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[대여할 멤버는 비어있을 수 없습니다.]"))
                .andDo(print());
    }

    @Test
    @DisplayName("대여 추가시 대상 멤버는 공백이면 안된다.")
    @WithMockMember
    void addOneFail5() throws Exception {
        //given
        memberRepository.save(Member.builder()
                .email(REQUEST_MEMBER_EMAIL)
                .username(REQUEST_MEMBER_USERNAME)
                .password(REQUEST_MEMBER_PASSWORD)
                .build());

        RentalCreateRequest request = RentalCreateRequest.builder()
                .product(RENTAL_PRODUCT)
                .count(RENTAL_COUNT)
                .responseMember("")
                .date(RENTAL_DATE)
                .time(RENTAL_TIME)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/rental")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[대여할 멤버는 비어있을 수 없습니다.]"))
                .andDo(print());
    }

    @Test
    @DisplayName("대여 추가시 과거 날짜이면 안된다.")
    @WithMockMember
    void addOneFail8() throws Exception {
        //given
        memberRepository.save(Member.builder()
                .email(REQUEST_MEMBER_EMAIL)
                .username(REQUEST_MEMBER_USERNAME)
                .password(REQUEST_MEMBER_PASSWORD)
                .build());

        RentalCreateRequest request = RentalCreateRequest.builder()
                .product(RENTAL_PRODUCT)
                .count(RENTAL_COUNT)
                .responseMember(REQUEST_MEMBER_USERNAME)
                .date(LocalDate.of(2000, 12, 20))
                .time(RENTAL_TIME)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/rental")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[과거 날짜일 수 없습니다.]"))
                .andDo(print());
    }

    @Test
    @DisplayName("대여 추가시 9시 이상의 시간이어야 한다.")
    @WithMockMember
    void addOneFail9() throws Exception {
        //given
        memberRepository.save(Member.builder()
                .email(REQUEST_MEMBER_EMAIL)
                .username(REQUEST_MEMBER_USERNAME)
                .password(REQUEST_MEMBER_PASSWORD)
                .build());

        RentalCreateRequest request = RentalCreateRequest.builder()
                .product(RENTAL_PRODUCT)
                .count(RENTAL_COUNT)
                .responseMember(REQUEST_MEMBER_USERNAME)
                .date(RENTAL_DATE)
                .time(8)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/rental")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[9시 이상의 시간이어야 합니다.]"))
                .andDo(print());
    }

    @Test
    @DisplayName("대여 추가시 22시 이하의 시간이어야 한다.")
    @WithMockMember
    void addOneFailRENTAL_COUNT0() throws Exception {
        //given
        memberRepository.save(Member.builder()
                .email(REQUEST_MEMBER_EMAIL)
                .username(REQUEST_MEMBER_USERNAME)
                .password(REQUEST_MEMBER_PASSWORD)
                .build());

        RentalCreateRequest request = RentalCreateRequest.builder()
                .product(RENTAL_PRODUCT)
                .count(RENTAL_COUNT)
                .responseMember(REQUEST_MEMBER_USERNAME)
                .date(RENTAL_DATE)
                .time(23)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/rental")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[22시 이하의 시간이어야 합니다.]"))
                .andDo(print());
    }

    /**
     * 대여 상세 조회 API 테스트 코드
     */

    @Test
    @DisplayName("대여 상세 조회")
    @WithMockMember
    void getOne() throws Exception {
        //given
        Member member1 = memberRepository.save(Member.builder()
                .email(REQUEST_MEMBER_EMAIL)
                .username(REQUEST_MEMBER_USERNAME)
                .password(REQUEST_MEMBER_PASSWORD)
                .build());

        Member member2 = memberRepository.save(Member.builder()
                .email(RESPONSE_MEMBER_EMAIL)
                .username(RESPONSE_MEMBER_USERNAME)
                .password(RESPONSE_MEMBER_PASSWORD)
                .build());

        Rental rental = rentalRepository.save(Rental.builder()
                .product(RENTAL_PRODUCT)
                .count(RENTAL_COUNT)
                .requestMember(member1)
                .responseMember(member2)
                .date(RENTAL_DATE)
                .time(RENTAL_TIME)
                .build());

        //expected
        mockMvc.perform(get("/rental/{id}", rental.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product").value(RENTAL_PRODUCT))
                .andExpect(jsonPath("$.count").value(RENTAL_COUNT))
                .andExpect(jsonPath("$.requestMember").value(REQUEST_MEMBER_USERNAME))
                .andExpect(jsonPath("$.responseMember").value(RESPONSE_MEMBER_USERNAME))
                .andExpect(jsonPath("$.date").value(RENTAL_DATE_STRING))
                .andExpect(jsonPath("$.time").value(RENTAL_TIME))
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 대여 상세 조회")
    @WithMockMember
    void getOneFail() throws Exception {
        //given
        Member member1 = memberRepository.save(Member.builder()
                .email(RESPONSE_MEMBER_EMAIL)
                .username(RESPONSE_MEMBER_USERNAME)
                .password(RESPONSE_MEMBER_PASSWORD)
                .build());

        Member member2 = memberRepository.save(Member.builder()
                .email(REQUEST_MEMBER_EMAIL)
                .username(REQUEST_MEMBER_USERNAME)
                .password(REQUEST_MEMBER_PASSWORD)
                .build());

        Rental rental = rentalRepository.save(Rental.builder()
                .product(RENTAL_PRODUCT)
                .count(RENTAL_COUNT)
                .requestMember(member1)
                .responseMember(member2)
                .date(RENTAL_DATE)
                .time(RENTAL_TIME)
                .build());

        //expected
        mockMvc.perform(get("/rental/{id}", rental.getId() + RENTAL_COUNT))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("존재하지 않는 대여입니다."))
                .andDo(print());
    }

    /**
     * 대여 수정 API 테스트 코드
     */
//TODO : 대여 수정시 대여한 사람고 ㅏ같아야함
    //TODO : 대여 수정시 존재하는 사람이ㅓㅇ야 함
    @Test
    @DisplayName("대여 수정")
    @WithMockMember
    void edit() throws Exception {
        //given
        Member member1 = memberRepository.findAll().getFirst();

        Member member2 = memberRepository.save(Member.builder()
                .email(REQUEST_MEMBER_EMAIL)
                .username(REQUEST_MEMBER_USERNAME)
                .password(REQUEST_MEMBER_PASSWORD)
                .build());

        memberRepository.save(Member.builder()
                .email("young@hanmail.com")
                .username("아영")
                .password("asdf")
                .build());

        Rental rental = rentalRepository.save(Rental.builder()
                .product(RENTAL_PRODUCT)
                .count(RENTAL_COUNT)
                .requestMember(member1)
                .responseMember(member2)
                .date(RENTAL_DATE)
                .time(RENTAL_TIME)
                .build());

        RentalEditRequest request = RentalEditRequest.builder()
                .product(RENTAL_PRODUCT)
                .count(RENTAL_COUNT)
                .responseMember("아영")
                .date(RENTAL_DATE)
                .time(RENTAL_TIME)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(put("/rental/{id}", rental.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("대여 수정시 품목 개수는 1개 이상이어야 한다.")
    @WithMockMember
    void editFailRENTAL_COUNT() throws Exception {
        //given
        Member member1 = memberRepository.save(Member.builder()
                .email(RESPONSE_MEMBER_EMAIL)
                .username(RESPONSE_MEMBER_USERNAME)
                .password(RESPONSE_MEMBER_PASSWORD)
                .build());

        Member member2 = memberRepository.save(Member.builder()
                .email(REQUEST_MEMBER_EMAIL)
                .username(REQUEST_MEMBER_USERNAME)
                .password(REQUEST_MEMBER_PASSWORD)
                .build());

        Member member3 = memberRepository.save(Member.builder()
                .email("young@hanmail.com")
                .username("아영")
                .password("asdf")
                .build());

        Rental rental = rentalRepository.save(Rental.builder()
                .product(RENTAL_PRODUCT)
                .count(RENTAL_COUNT)
                .requestMember(member1)
                .responseMember(member2)
                .date(RENTAL_DATE)
                .time(RENTAL_TIME)
                .build());

        RentalEditRequest request = RentalEditRequest.builder()
                .product(RENTAL_PRODUCT)
                .count(0)
                .responseMember("아영")
                .date(RENTAL_DATE)
                .time(RENTAL_TIME)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(put("/rental/{id}", rental.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[개수는 1개 이상이어야 합니다.]"))
                .andDo(print());
    }

    @Test
    @DisplayName("대여 수정시 과거 날짜일 수 없습니다.")
    @WithMockMember
    void editFail2() throws Exception {
        //given
        Member member1 = memberRepository.findAll().getFirst();

        Member member2 = memberRepository.save(Member.builder()
                .email(REQUEST_MEMBER_EMAIL)
                .username(REQUEST_MEMBER_USERNAME)
                .password(REQUEST_MEMBER_PASSWORD)
                .build());

        memberRepository.save(Member.builder()
                .email("young@hanmail.com")
                .username("아영")
                .password("asdf")
                .build());

        Rental rental = rentalRepository.save(Rental.builder()
                .product(RENTAL_PRODUCT)
                .count(RENTAL_COUNT)
                .requestMember(member1)
                .responseMember(member2)
                .date(RENTAL_DATE)
                .time(RENTAL_TIME)
                .build());

        RentalEditRequest request = RentalEditRequest.builder()
                .product(RENTAL_PRODUCT)
                .count(RENTAL_COUNT)
                .responseMember("아영")
                .date(LocalDate.of(2000, 12, 20))
                .time(RENTAL_TIME)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(put("/rental/{id}", rental.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[과거 날짜일 수 없습니다.]"))
                .andDo(print());
    }

    @Test
    @DisplayName("대여 수정시 9시 이상 이어야 합니다.")
    @WithMockMember
    void editFail3() throws Exception {
        //given
        Member member1 = memberRepository.save(Member.builder()
                .email(RESPONSE_MEMBER_EMAIL)
                .username(RESPONSE_MEMBER_USERNAME)
                .password(RESPONSE_MEMBER_PASSWORD)
                .build());

        Member member2 = memberRepository.save(Member.builder()
                .email(REQUEST_MEMBER_EMAIL)
                .username(REQUEST_MEMBER_USERNAME)
                .password(REQUEST_MEMBER_PASSWORD)
                .build());

        memberRepository.save(Member.builder()
                .email("young@hanmail.com")
                .username("아영")
                .password("asdf")
                .build());

        Rental rental = rentalRepository.save(Rental.builder()
                .product(RENTAL_PRODUCT)
                .count(RENTAL_COUNT)
                .requestMember(member1)
                .responseMember(member2)
                .date(RENTAL_DATE)
                .time(RENTAL_TIME)
                .build());

        RentalEditRequest request = RentalEditRequest.builder()
                .product(RENTAL_PRODUCT)
                .count(RENTAL_COUNT)
                .responseMember("아영")
                .date(RENTAL_DATE)
                .time(8)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(put("/rental/{id}", rental.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[9시 이상의 시간이어야 합니다.]"))
                .andDo(print());
    }

    @Test
    @DisplayName("대여 수정시 22시 이하 이어야 합니다.")
    @WithMockMember
    void editFail4() throws Exception {
        //given
        Member member1 = memberRepository.save(Member.builder()
                .email(RESPONSE_MEMBER_EMAIL)
                .username(RESPONSE_MEMBER_USERNAME)
                .password(RESPONSE_MEMBER_PASSWORD)
                .build());

        Member member2 = memberRepository.save(Member.builder()
                .email(REQUEST_MEMBER_EMAIL)
                .username(REQUEST_MEMBER_USERNAME)
                .password(REQUEST_MEMBER_PASSWORD)
                .build());

        Member member3 = memberRepository.save(Member.builder()
                .email("young@hanmail.com")
                .username("아영")
                .password("asdf")
                .build());

        Rental rental = rentalRepository.save(Rental.builder()
                .product(RENTAL_PRODUCT)
                .count(RENTAL_COUNT)
                .requestMember(member1)
                .responseMember(member2)
                .date(RENTAL_DATE)
                .time(RENTAL_TIME)
                .build());

        RentalEditRequest request = RentalEditRequest.builder()
                .product(RENTAL_PRODUCT)
                .count(RENTAL_COUNT)
                .responseMember("아영")
                .date(RENTAL_DATE)
                .time(23)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(put("/rental/{id}", rental.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[22시 이하의 시간이어야 합니다.]"))
                .andDo(print());
    }

    /**
     * 대여 삭제 API 테스트 코드
     */

    @Test
    @DisplayName("대여 삭제")
    @WithMockMember
    void deleteRENTAL_COUNT() throws Exception {
        //given
        Member member1 = memberRepository.findAll().getFirst();

        Member member2 = memberRepository.save(Member.builder()
                .email(RESPONSE_MEMBER_EMAIL)
                .username(RESPONSE_MEMBER_USERNAME)
                .password(RESPONSE_MEMBER_PASSWORD)
                .build());

        Rental rental = rentalRepository.save(Rental.builder()
                .product(RENTAL_PRODUCT)
                .count(RENTAL_COUNT)
                .requestMember(member1)
                .responseMember(member2)
                .date(RENTAL_DATE)
                .time(RENTAL_TIME)
                .build());

        //expected
        mockMvc.perform(delete("/rental/{id}", rental.getId()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 대여 삭제")
    @WithMockMember
    void deleteFail() throws Exception {
        //given
        Member member1 = memberRepository.findAll().getFirst();

        Member member2 = memberRepository.save(Member.builder()
                .email(RESPONSE_MEMBER_EMAIL)
                .username(RESPONSE_MEMBER_USERNAME)
                .password(RESPONSE_MEMBER_PASSWORD)
                .build());

        Rental rental = rentalRepository.save(Rental.builder()
                .product(RENTAL_PRODUCT)
                .count(RENTAL_COUNT)
                .requestMember(member1)
                .responseMember(member2)
                .date(RENTAL_DATE)
                .time(RENTAL_TIME)
                .build());

        //expected
        mockMvc.perform(delete("/rental/{id}", rental.getId() + RENTAL_COUNT))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}