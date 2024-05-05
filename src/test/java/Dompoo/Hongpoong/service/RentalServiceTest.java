package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.domain.Rental;
import Dompoo.Hongpoong.exception.*;
import Dompoo.Hongpoong.repository.MemberRepository;
import Dompoo.Hongpoong.repository.RentalRepository;
import Dompoo.Hongpoong.request.rental.RentalCreateRequest;
import Dompoo.Hongpoong.request.rental.RentalEditRequest;
import Dompoo.Hongpoong.response.RentalResponse;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RentalServiceTest {

    @Autowired
    private RentalService service;
    @Autowired
    private RentalRepository rentalRepository;
    @Autowired
    private MemberRepository memberRepository;

    private static final String MEM1_USERNAME = "창근";
    private static final String MEM1_EMAIL = "dompoo@gmail.com";
    private static final String MEM1_PASSWORD = "1234";
    private static final String MEM2_USERNAME = "윤호";
    private static final String MEM2_EMAIL = "yoonH@naver.com";
    private static final String MEM2_PASSWORD = "qwer";
    private static final String MEM3_USERNAME = "2아영";
    private static final String MEM3_EMAIL = "young@hanmail.com";
    private static final String MEM3_PASSWORD = "asdf";
    private static final String RENTAL_PRODUCT = "장구";
    private static final LocalDate RENTAL_DATE = LocalDate.of(2000, 12, 20);

    @BeforeEach
    void setUp() {
        rentalRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("대여 리스트 조회")
    void list() {
        //given
        Member member1 = memberRepository.save(Member.builder()
                .username(MEM1_USERNAME)
                .email(MEM1_EMAIL)
                .password(MEM1_PASSWORD)
                .club(Member.Club.AKBAN)
                .build());

        Member member2 = memberRepository.save(Member.builder()
                .username(MEM2_USERNAME)
                .email(MEM2_EMAIL)
                .password(MEM2_PASSWORD)
                .club(Member.Club.DEULNEOK)
                .build());

        Member member3 = memberRepository.save(Member.builder()
                .username(MEM2_USERNAME)
                .email(MEM2_EMAIL)
                .password(MEM2_PASSWORD)
                .club(Member.Club.DEULNEOK)
                .build());

        Rental rental1 = rentalRepository.save(Rental.builder()
                .product(RENTAL_PRODUCT)
                .count(1)
                .responseMember(member1)
                .requestMember(member2)
                .date(RENTAL_DATE)
                .time(13)
                .build());

        Rental rental2 = rentalRepository.save(Rental.builder()
                .product("소고")
                .count(4)
                .responseMember(member2)
                .requestMember(member3)
                .date(LocalDate.of(2000, 12, 30))
                .time(15)
                .build());

        //when
        List<RentalResponse> list = service.getList(member1.getId());

        //then
        assertEquals(list.size(), 1);
        assertEquals(list.getFirst().getId(), rental1.getId());
        assertEquals(list.getFirst().getProduct(), RENTAL_PRODUCT);
        assertEquals(list.getFirst().getCount(), 1);
        assertEquals(list.getFirst().getResponseMember(), MEM1_USERNAME);
        assertEquals(list.getFirst().getRequestMember(), MEM2_USERNAME);
        assertEquals(list.getFirst().getDate(), RENTAL_DATE);
        assertEquals(list.getFirst().getTime(), 13);
    }

    @Test
    @DisplayName("대여 추가")
    void addOne() {
        //given
        Member responseMember = memberRepository.save(Member.builder()
                .username(MEM1_USERNAME)
                .email(MEM1_EMAIL)
                .password(MEM1_PASSWORD)
                .build());

        Member requestMember = memberRepository.save(Member.builder()
                .username(MEM2_USERNAME)
                .email(MEM2_EMAIL)
                .password(MEM2_PASSWORD)
                .build());

        RentalCreateRequest request = RentalCreateRequest.builder()
                .product(RENTAL_PRODUCT)
                .count(1)
                .responseMember(responseMember.getUsername())
                .date(RENTAL_DATE)
                .time(13)
                .build();

        //when
        service.addRental(requestMember.getId(), request);

        //then
        assertEquals(rentalRepository.count(), 1);
        assertEquals(rentalRepository.findAll().getFirst().getProduct(), RENTAL_PRODUCT);
        assertEquals(rentalRepository.findAll().getFirst().getCount(), 1);
        assertEquals(rentalRepository.findAll().getFirst().getResponseMember().getId(), responseMember.getId());
        assertEquals(rentalRepository.findAll().getFirst().getRequestMember().getId(), requestMember.getId());
        assertEquals(rentalRepository.findAll().getFirst().getDate(), RENTAL_DATE);
        assertEquals(rentalRepository.findAll().getFirst().getTime(), 13);
    }

    @Test
    @DisplayName("존재하지 않는 유저가 대여 추가 시도")
    void addOneFail() {
        //given
        Member responseMember = memberRepository.save(Member.builder()
                .username(MEM1_USERNAME)
                .email(MEM1_EMAIL)
                .password(MEM1_PASSWORD)
                .build());

        Member requestMember = memberRepository.save(Member.builder()
                .username(MEM2_USERNAME)
                .email(MEM2_EMAIL)
                .password(MEM2_PASSWORD)
                .build());

        RentalCreateRequest request = RentalCreateRequest.builder()
                .product(RENTAL_PRODUCT)
                .count(1)
                .responseMember(responseMember.getUsername())
                .date(RENTAL_DATE)
                .time(13)
                .build();

        //when
        MemberNotFound e = assertThrows(MemberNotFound.class,
                () -> service.addRental(requestMember.getId() + 1, request));

        //then
        assertEquals(e.getMessage(), "존재하지 않는 유저입니다.");
        assertEquals(e.statusCode(), "404");
    }

    @Test
    @DisplayName("대여 추가시 자신에게 빌릴 수 없다.")
    void addOneFail1() {
        //given
        Member member = memberRepository.save(Member.builder()
                .username(MEM1_USERNAME)
                .email(MEM1_EMAIL)
                .password(MEM1_PASSWORD)
                .build());

        RentalCreateRequest request = RentalCreateRequest.builder()
                .product(RENTAL_PRODUCT)
                .count(1)
                .responseMember(member.getUsername())
                .date(RENTAL_DATE)
                .time(13)
                .build();

        //when
        SelfRentalException e = assertThrows(SelfRentalException.class, () ->
                service.addRental(member.getId(), request));

        //then
        assertEquals(e.getMessage(), "자신에게 대여할 수 없습니다.");
        assertEquals(e.statusCode(), "400");
    }

    @Test
    @DisplayName("대여 상세 조회")
    void getOne() {
        //given
        Member responseMember = memberRepository.save(Member.builder()
                .username(MEM1_USERNAME)
                .email(MEM1_EMAIL)
                .password(MEM1_PASSWORD)
                .build());

        Member requestMember = memberRepository.save(Member.builder()
                .username(MEM2_USERNAME)
                .email(MEM2_EMAIL)
                .password(MEM2_PASSWORD)
                .build());

        Rental rental = rentalRepository.save(Rental.builder()
                .product(RENTAL_PRODUCT)
                .count(1)
                .responseMember(responseMember)
                .requestMember(requestMember)
                .date(RENTAL_DATE)
                .time(13)
                .build());

        //when
        RentalResponse response = service.getDetail(rental.getId());

        //then
        assertEquals(response.getId(), rental.getId());
        assertEquals(response.getProduct(), RENTAL_PRODUCT);
        assertEquals(response.getCount(), 1);
        assertEquals(response.getResponseMember(), MEM1_USERNAME);
        assertEquals(response.getRequestMember(), MEM2_USERNAME);
        assertEquals(response.getDate(), RENTAL_DATE);
        assertEquals(response.getTime(), 13);
    }

    @Test
    @DisplayName("존재하지 않는 대여 상세 조회")
    void getOneFail() {
        Member responseMember = memberRepository.save(Member.builder()
                .username(MEM1_USERNAME)
                .email(MEM1_EMAIL)
                .password(MEM1_PASSWORD)
                .build());

        Member requestMember = memberRepository.save(Member.builder()
                .username(MEM2_USERNAME)
                .email(MEM2_EMAIL)
                .password(MEM2_PASSWORD)
                .build());

        Rental rental = rentalRepository.save(Rental.builder()
                .product(RENTAL_PRODUCT)
                .count(1)
                .responseMember(responseMember)
                .requestMember(requestMember)
                .date(RENTAL_DATE)
                .time(13)
                .build());

        //when
        RentalNotFound e = assertThrows(RentalNotFound.class,
                () -> service.getDetail(rental.getId() + 1));

        //then
        assertEquals(e.getMessage(), "존재하지 않는 대여입니다.");
        assertEquals(e.statusCode(), "404");
    }

    @Test
    @DisplayName("대여 전체 수정")
    void edit1() {
        //given
        Member responseMember = memberRepository.save(Member.builder()
                .username(MEM1_USERNAME)
                .email(MEM1_EMAIL)
                .password(MEM1_PASSWORD)
                .build());

        Member requestMember = memberRepository.save(Member.builder()
                .username(MEM2_USERNAME)
                .email(MEM2_EMAIL)
                .password(MEM2_PASSWORD)
                .build());

        Member editMember = memberRepository.save(Member.builder()
                .username(MEM3_USERNAME)
                .email(MEM3_EMAIL)
                .password(MEM3_PASSWORD)
                .build());

        Rental rental = rentalRepository.save(Rental.builder()
                .product(RENTAL_PRODUCT)
                .count(1)
                .responseMember(responseMember)
                .requestMember(requestMember)
                .date(RENTAL_DATE)
                .time(13)
                .build());

        RentalEditRequest request = RentalEditRequest.builder()
                .product("소고")
                .count(2)
                .responseMember(editMember.getUsername())
                .date(LocalDate.of(2000, 12, 30))
                .time(15)
                .build();

        //when
        service.editRental(requestMember.getId(), rental.getId(), request);

        //then
        Rental find = assertDoesNotThrow(() -> rentalRepository.findById(rental.getId())
                .orElseThrow());
        assertEquals(find.getProduct(), "소고");
        assertEquals(find.getCount(), 2);
        assertEquals(find.getResponseMember().getId(), editMember.getId());
        assertEquals(find.getRequestMember().getId(), requestMember.getId());
        assertEquals(find.getDate(), LocalDate.of(2000, 12, 30));
        assertEquals(find.getTime(), 15);
    }

    @Test
    @DisplayName("대여 일부 수정")
    void edit2() {
        //given
        Member responseMember = memberRepository.save(Member.builder()
                .username(MEM1_USERNAME)
                .email(MEM1_EMAIL)
                .password(MEM1_PASSWORD)
                .build());

        Member requestMember = memberRepository.save(Member.builder()
                .username(MEM2_USERNAME)
                .email(MEM2_EMAIL)
                .password(MEM2_PASSWORD)
                .build());

        Rental rental = rentalRepository.save(Rental.builder()
                .product(RENTAL_PRODUCT)
                .count(1)
                .responseMember(responseMember)
                .requestMember(requestMember)
                .date(RENTAL_DATE)
                .time(13)
                .build());

        RentalEditRequest request = RentalEditRequest.builder()
                .product("소고")
                .count(2)
                .build();

        //when
        service.editRental(requestMember.getId(), rental.getId(), request);

        //then
        Rental find = assertDoesNotThrow(() -> rentalRepository.findById(rental.getId())
                .orElseThrow());
        assertEquals(find.getProduct(), "소고");
        assertEquals(find.getCount(), 2);
        assertEquals(find.getResponseMember().getId(), responseMember.getId());
        assertEquals(find.getRequestMember().getId(), requestMember.getId());
        assertEquals(find.getDate(), RENTAL_DATE);
        assertEquals(find.getTime(), 13);
    }

    @Test
    @DisplayName("존재하지 않는 대여 수정 실패")
    void editFail1() {
        //given
        Member responseMember = memberRepository.save(Member.builder()
                .username(MEM1_USERNAME)
                .email(MEM1_EMAIL)
                .password(MEM1_PASSWORD)
                .build());

        Member requestMember = memberRepository.save(Member.builder()
                .username(MEM2_USERNAME)
                .email(MEM2_EMAIL)
                .password(MEM2_PASSWORD)
                .build());

        Rental rental = rentalRepository.save(Rental.builder()
                .product(RENTAL_PRODUCT)
                .count(1)
                .responseMember(responseMember)
                .requestMember(requestMember)
                .date(RENTAL_DATE)
                .time(13)
                .build());

        RentalEditRequest request = RentalEditRequest.builder()
                .product("소고")
                .count(2)
                .responseMember(responseMember.getUsername())
                .date(LocalDate.of(2000, 12, 30))
                .time(15)
                .build();

        //when
        RentalNotFound e = assertThrows(RentalNotFound.class, () ->
                service.editRental(requestMember.getId(), rental.getId() + 1, request));

        //then
        assertEquals(e.getMessage(), "존재하지 않는 대여입니다.");
        assertEquals(e.statusCode(), "404");
    }

    @Test
    @DisplayName("존재하지 않는 유저가 대여 수정 시도")
    void editFail2() {
        //given
        Member responseMember = memberRepository.save(Member.builder()
                .username(MEM1_USERNAME)
                .email(MEM1_EMAIL)
                .password(MEM1_PASSWORD)
                .build());

        Member requestMember = memberRepository.save(Member.builder()
                .username(MEM2_USERNAME)
                .email(MEM2_EMAIL)
                .password(MEM2_PASSWORD)
                .build());

        Rental rental = rentalRepository.save(Rental.builder()
                .product(RENTAL_PRODUCT)
                .count(1)
                .responseMember(responseMember)
                .requestMember(requestMember)
                .date(RENTAL_DATE)
                .time(13)
                .build());

        RentalEditRequest request = RentalEditRequest.builder()
                .product("소고")
                .count(2)
                .responseMember(responseMember.getUsername())
                .date(LocalDate.of(2000, 12, 30))
                .time(15)
                .build();

        //when
        EditFailException e = assertThrows(EditFailException.class, () ->
                service.editRental(requestMember.getId() + 1, rental.getId(), request));

        //then
        assertEquals(e.getMessage(), "수정할 수 없습니다.");
        assertEquals(e.statusCode(), "403");
    }

    @Test
    @DisplayName("대여자가 아닌 유저가 대여 수정 시도시 실패")
    void editFail3() {
        //given
        Member responseMember = memberRepository.save(Member.builder()
                .username(MEM1_USERNAME)
                .email(MEM1_EMAIL)
                .password(MEM1_PASSWORD)
                .build());

        Member requestMember = memberRepository.save(Member.builder()
                .username(MEM2_USERNAME)
                .email(MEM2_EMAIL)
                .password(MEM2_PASSWORD)
                .build());

        Member anotherMember = memberRepository.save(Member.builder()
                .username(MEM3_USERNAME)
                .email(MEM3_EMAIL)
                .password(MEM3_PASSWORD)
                .build());

        Rental rental = rentalRepository.save(Rental.builder()
                .product(RENTAL_PRODUCT)
                .count(1)
                .responseMember(responseMember)
                .requestMember(requestMember)
                .date(RENTAL_DATE)
                .time(13)
                .build());

        RentalEditRequest request = RentalEditRequest.builder()
                .product("소고")
                .count(2)
                .responseMember(responseMember.getUsername())
                .date(LocalDate.of(2000, 12, 30))
                .time(15)
                .build();

        //when
        EditFailException e = assertThrows(EditFailException.class, () ->
                service.editRental(anotherMember.getId(), rental.getId(), request));

        //then
        assertEquals(e.getMessage(), "수정할 수 없습니다.");
        assertEquals(e.statusCode(), "403");
    }

    @Test
    @DisplayName("존재하지 않는 유저로 대여 수정 시도시 실패")
    void editFail4() {
        //given
        Member responseMember = memberRepository.save(Member.builder()
                .username(MEM1_USERNAME)
                .email(MEM1_EMAIL)
                .password(MEM1_PASSWORD)
                .build());

        Member requestMember = memberRepository.save(Member.builder()
                .username(MEM2_USERNAME)
                .email(MEM2_EMAIL)
                .password(MEM2_PASSWORD)
                .build());

        Rental rental = rentalRepository.save(Rental.builder()
                .product(RENTAL_PRODUCT)
                .count(1)
                .responseMember(responseMember)
                .requestMember(requestMember)
                .date(RENTAL_DATE)
                .time(13)
                .build());

        RentalEditRequest request = RentalEditRequest.builder()
                .product("소고")
                .count(2)
                .responseMember("이상한 사람")
                .date(LocalDate.of(2000, 12, 30))
                .time(15)
                .build();

        //when
        MemberNotFound e = assertThrows(MemberNotFound.class, () ->
                service.editRental(requestMember.getId(), rental.getId(), request));

        //then
        assertEquals(e.getMessage(), "존재하지 않는 유저입니다.");
        assertEquals(e.statusCode(), "404");
    }

    @Test
    @DisplayName("대여 삭제")
    void delete() {
        //given
        Member responseMember = memberRepository.save(Member.builder()
                .username(MEM1_USERNAME)
                .email(MEM1_EMAIL)
                .password(MEM1_PASSWORD)
                .build());

        Member requestMember = memberRepository.save(Member.builder()
                .username(MEM2_USERNAME)
                .email(MEM2_EMAIL)
                .password(MEM2_PASSWORD)
                .build());

        Rental rental = rentalRepository.save(Rental.builder()
                .product(RENTAL_PRODUCT)
                .count(1)
                .responseMember(responseMember)
                .requestMember(requestMember)
                .date(RENTAL_DATE)
                .time(13)
                .build());

        //when
        service.deleteRental(requestMember.getId(), rental.getId());

        //then
        assertEquals(rentalRepository.count(), 0);
    }

    @Test
    @DisplayName("존재하지 않는 대여 삭제")
    void deleteFail1() {
        Member responseMember = memberRepository.save(Member.builder()
                .username(MEM1_USERNAME)
                .email(MEM1_EMAIL)
                .password(MEM1_PASSWORD)
                .build());

        Member requestMember = memberRepository.save(Member.builder()
                .username(MEM2_USERNAME)
                .email(MEM2_EMAIL)
                .password(MEM2_PASSWORD)
                .build());

        Rental rental = rentalRepository.save(Rental.builder()
                .product(RENTAL_PRODUCT)
                .count(1)
                .responseMember(responseMember)
                .requestMember(requestMember)
                .date(RENTAL_DATE)
                .time(13)
                .build());

        //when
        RentalNotFound e = assertThrows(RentalNotFound.class, () ->
                service.deleteRental(requestMember.getId(), rental.getId() + 1));

        //then
        assertEquals(e.getMessage(), "존재하지 않는 대여입니다.");
        assertEquals(e.statusCode(), "404");
    }

    @Test
    @DisplayName("존재하지 않는 유저가 대여 삭제 시도")
    void deleteFail2() {
        Member responseMember = memberRepository.save(Member.builder()
                .username(MEM1_USERNAME)
                .email(MEM1_EMAIL)
                .password(MEM1_PASSWORD)
                .build());

        Member requestMember = memberRepository.save(Member.builder()
                .username(MEM2_USERNAME)
                .email(MEM2_EMAIL)
                .password(MEM2_PASSWORD)
                .build());

        Rental rental = rentalRepository.save(Rental.builder()
                .product(RENTAL_PRODUCT)
                .count(1)
                .responseMember(responseMember)
                .requestMember(requestMember)
                .date(RENTAL_DATE)
                .time(13)
                .build());

        //when
        DeleteFailException e = assertThrows(DeleteFailException.class, () ->
                service.deleteRental(requestMember.getId() + 1, rental.getId()));

        //then
        assertEquals(e.getMessage(), "삭제할 수 없습니다.");
        assertEquals(e.statusCode(), "403");
    }

    @Test
    @DisplayName("대여자가 아닌 유저가 대여 삭제 시도시 실패")
    void deleteFail3() {
        Member responseMember = memberRepository.save(Member.builder()
                .username(MEM1_USERNAME)
                .email(MEM1_EMAIL)
                .password(MEM1_PASSWORD)
                .build());

        Member requestMember = memberRepository.save(Member.builder()
                .username(MEM2_USERNAME)
                .email(MEM2_EMAIL)
                .password(MEM2_PASSWORD)
                .build());

        Member anotherMember = memberRepository.save(Member.builder()
                .username(MEM3_USERNAME)
                .email(MEM3_EMAIL)
                .password(MEM3_PASSWORD)
                .build());

        Rental rental = rentalRepository.save(Rental.builder()
                .product(RENTAL_PRODUCT)
                .count(1)
                .responseMember(responseMember)
                .requestMember(requestMember)
                .date(RENTAL_DATE)
                .time(13)
                .build());

        //when
        DeleteFailException e = assertThrows(DeleteFailException.class, () ->
                service.deleteRental(anotherMember.getId(), rental.getId()));

        //then
        assertEquals(e.getMessage(), "삭제할 수 없습니다.");
        assertEquals(e.statusCode(), "403");
    }

    @Test
    @DisplayName("대여 로그 조회")
    void log() {
        //given
        Member member1 = memberRepository.save(Member.builder()
                .username(MEM1_USERNAME)
                .email(MEM1_EMAIL)
                .password(MEM1_PASSWORD)
                .club(Member.Club.AKBAN)
                .build());

        Member member2 = memberRepository.save(Member.builder()
                .username(MEM2_USERNAME)
                .email(MEM2_EMAIL)
                .password(MEM2_PASSWORD)
                .club(Member.Club.DEULNEOK)
                .build());

        Member member3 = memberRepository.save(Member.builder()
                .username(MEM3_USERNAME)
                .email(MEM2_EMAIL)
                .password(MEM2_PASSWORD)
                .club(Member.Club.DEULNEOK)
                .build());

        Rental rental1 = rentalRepository.save(Rental.builder()
                .product(RENTAL_PRODUCT)
                .count(1)
                .responseMember(member1)
                .requestMember(member2)
                .date(RENTAL_DATE)
                .time(13)
                .build());

        Rental rental2 = rentalRepository.save(Rental.builder()
                .product("소고")
                .count(4)
                .responseMember(member2)
                .requestMember(member3)
                .date(LocalDate.of(2000, 12, 30))
                .time(15)
                .build());

        //when
        List<RentalResponse> list = service.getLog();

        //then
        assertEquals(list.size(), 2);
        assertEquals(list.getFirst().getId(), rental1.getId());
        assertEquals(list.getFirst().getProduct(), RENTAL_PRODUCT);
        assertEquals(list.getFirst().getCount(), 1);
        assertEquals(list.getFirst().getResponseMember(), MEM1_USERNAME);
        assertEquals(list.getFirst().getRequestMember(), MEM2_USERNAME);
        assertEquals(list.getFirst().getDate(), RENTAL_DATE);
        assertEquals(list.getFirst().getTime(), 13);
        assertEquals(list.get(1).getId(), rental2.getId());
        assertEquals(list.get(1).getProduct(), "소고");
        assertEquals(list.get(1).getCount(), 4);
        assertEquals(list.get(1).getResponseMember(), MEM2_USERNAME);
        assertEquals(list.get(1).getRequestMember(), MEM3_USERNAME);
        assertEquals(list.get(1).getDate(), LocalDate.of(2000, 12, 30));
        assertEquals(list.get(1).getTime(), 15);
    }
}