package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.domain.Rental;
import Dompoo.Hongpoong.exception.DeleteFailException;
import Dompoo.Hongpoong.exception.EditFailException;
import Dompoo.Hongpoong.exception.MemberNotFound;
import Dompoo.Hongpoong.exception.RentalNotFound;
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

    @BeforeEach
    void setUp() {
        rentalRepository.deleteAll();
    }

    @Test
    @DisplayName("예약 리스트 조회")
    void list() {
        //given
        Member fromMember = memberRepository.save(Member.builder()
                .username("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        Member toMember = memberRepository.save(Member.builder()
                .username("윤호")
                .email("yoonH@naver.com")
                .password("qwer")
                .build());

        Rental rental1 = rentalRepository.save(Rental.builder()
                .product("장구")
                .count(1)
                .fromMember(fromMember)
                .toMember(toMember)
                .date(LocalDate.of(2000, 12, 20))
                .time(13)
                .build());

        Rental rental2 = rentalRepository.save(Rental.builder()
                .product("소고")
                .count(4)
                .fromMember(fromMember)
                .toMember(toMember)
                .date(LocalDate.of(2000, 12, 30))
                .time(15)
                .build());

        //when
        List<RentalResponse> list = service.getList();

        //then
        assertEquals(rentalRepository.count(), 2);
        assertEquals(list.getFirst().getId(), rental1.getId());
        assertEquals(list.getFirst().getProduct(), "장구");
        assertEquals(list.getFirst().getCount(), 1);
        assertEquals(list.getFirst().getFromMember(), "창근");
        assertEquals(list.getFirst().getToMember(), "윤호");
        assertEquals(list.getFirst().getDate(), LocalDate.of(2000, 12, 20));
        assertEquals(list.getFirst().getTime(), 13);
        assertEquals(list.get(1).getId(), rental2.getId());
        assertEquals(list.get(1).getProduct(), "소고");
        assertEquals(list.get(1).getCount(), 4);
        assertEquals(list.get(1).getFromMember(), "창근");
        assertEquals(list.get(1).getToMember(), "윤호");
        assertEquals(list.get(1).getDate(), LocalDate.of(2000, 12, 30));
        assertEquals(list.get(1).getTime(), 15);
    }

    @Test
    @DisplayName("예약 추가")
    void addOne() {
        //given
        Member fromMember = memberRepository.save(Member.builder()
                .username("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        Member toMember = memberRepository.save(Member.builder()
                .username("윤호")
                .email("yoonH@naver.com")
                .password("qwer")
                .build());

        RentalCreateRequest request = RentalCreateRequest.builder()
                .product("장구")
                .count(1)
                .fromMember(fromMember.getUsername())
                .date(LocalDate.of(2000, 12, 20))
                .time(13)
                .build();

        //when
        service.addRental(toMember.getId(), request);

        //then
        assertEquals(rentalRepository.count(), 1);
        assertEquals(rentalRepository.findAll().getFirst().getProduct(), "장구");
        assertEquals(rentalRepository.findAll().getFirst().getCount(), 1);
        assertEquals(rentalRepository.findAll().getFirst().getFromMember().getId(), fromMember.getId());
        assertEquals(rentalRepository.findAll().getFirst().getToMember().getId(), toMember.getId());
        assertEquals(rentalRepository.findAll().getFirst().getDate(), LocalDate.of(2000, 12, 20));
        assertEquals(rentalRepository.findAll().getFirst().getTime(), 13);
    }

    @Test
    @DisplayName("존재하지 않는 유저가 예약 추가 시도")
    void addOneFail() {
        //given
        Member fromMember = memberRepository.save(Member.builder()
                .username("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        Member toMember = memberRepository.save(Member.builder()
                .username("윤호")
                .email("yoonH@naver.com")
                .password("qwer")
                .build());

        RentalCreateRequest request = RentalCreateRequest.builder()
                .product("장구")
                .count(1)
                .fromMember(fromMember.getUsername())
                .date(LocalDate.of(2000, 12, 20))
                .time(13)
                .build();

        //when
        MemberNotFound e = assertThrows(MemberNotFound.class,
                () -> service.addRental(toMember.getId() + 1, request));

        //then
        assertEquals(e.getMessage(), "존재하지 않는 유저입니다.");
        assertEquals(e.statusCode(), "404");
    }

    @Test
    @DisplayName("예약 상세 조회")
    void getOne() {
        //given
        Member fromMember = memberRepository.save(Member.builder()
                .username("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        Member toMember = memberRepository.save(Member.builder()
                .username("윤호")
                .email("yoonH@naver.com")
                .password("qwer")
                .build());

        Rental rental = rentalRepository.save(Rental.builder()
                .product("장구")
                .count(1)
                .fromMember(fromMember)
                .toMember(toMember)
                .date(LocalDate.of(2000, 12, 20))
                .time(13)
                .build());

        //when
        RentalResponse response = service.getDetail(rental.getId());

        //then
        assertEquals(response.getId(), rental.getId());
        assertEquals(response.getProduct(), "장구");
        assertEquals(response.getCount(), 1);
        assertEquals(response.getFromMember(), "창근");
        assertEquals(response.getToMember(), "윤호");
        assertEquals(response.getDate(), LocalDate.of(2000, 12, 20));
        assertEquals(response.getTime(), 13);
    }

    @Test
    @DisplayName("존재하지 않는 예약 상세 조회")
    void getOneFail() {
        Member fromMember = memberRepository.save(Member.builder()
                .username("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        Member toMember = memberRepository.save(Member.builder()
                .username("윤호")
                .email("yoonH@naver.com")
                .password("qwer")
                .build());

        Rental rental = rentalRepository.save(Rental.builder()
                .product("장구")
                .count(1)
                .fromMember(fromMember)
                .toMember(toMember)
                .date(LocalDate.of(2000, 12, 20))
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
        Member fromMember = memberRepository.save(Member.builder()
                .username("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        Member toMember = memberRepository.save(Member.builder()
                .username("윤호")
                .email("yoonH@naver.com")
                .password("qwer")
                .build());

        Member editMember = memberRepository.save(Member.builder()
                .username("아영")
                .email("young@hanmail.com")
                .password("asdf")
                .build());

        Rental rental = rentalRepository.save(Rental.builder()
                .product("장구")
                .count(1)
                .fromMember(fromMember)
                .toMember(toMember)
                .date(LocalDate.of(2000, 12, 20))
                .time(13)
                .build());

        RentalEditRequest request = RentalEditRequest.builder()
                .product("소고")
                .count(2)
                .fromMember(editMember.getUsername())
                .date(LocalDate.of(2000, 12, 30))
                .time(15)
                .build();

        //when
        service.editRental(toMember.getId(), rental.getId(), request);

        //then
        Rental find = assertDoesNotThrow(() -> rentalRepository.findById(rental.getId())
                .orElseThrow());
        assertEquals(find.getProduct(), "소고");
        assertEquals(find.getCount(), 2);
        assertEquals(find.getFromMember().getId(), editMember.getId());
        assertEquals(find.getToMember().getId(), toMember.getId());
        assertEquals(find.getDate(), LocalDate.of(2000, 12, 30));
        assertEquals(find.getTime(), 15);
    }

    @Test
    @DisplayName("대여 일부 수정")
    void edit2() {
        //given
        Member fromMember = memberRepository.save(Member.builder()
                .username("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        Member toMember = memberRepository.save(Member.builder()
                .username("윤호")
                .email("yoonH@naver.com")
                .password("qwer")
                .build());

        Rental rental = rentalRepository.save(Rental.builder()
                .product("장구")
                .count(1)
                .fromMember(fromMember)
                .toMember(toMember)
                .date(LocalDate.of(2000, 12, 20))
                .time(13)
                .build());

        RentalEditRequest request = RentalEditRequest.builder()
                .product("소고")
                .count(2)
                .build();

        //when
        service.editRental(toMember.getId(), rental.getId(), request);

        //then
        Rental find = assertDoesNotThrow(() -> rentalRepository.findById(rental.getId())
                .orElseThrow());
        assertEquals(find.getProduct(), "소고");
        assertEquals(find.getCount(), 2);
        assertEquals(find.getFromMember().getId(), fromMember.getId());
        assertEquals(find.getToMember().getId(), toMember.getId());
        assertEquals(find.getDate(), LocalDate.of(2000, 12, 20));
        assertEquals(find.getTime(), 13);
    }

    @Test
    @DisplayName("존재하지 않는 대여 수정 실패")
    void editFail1() {
        //given
        Member fromMember = memberRepository.save(Member.builder()
                .username("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        Member toMember = memberRepository.save(Member.builder()
                .username("윤호")
                .email("yoonH@naver.com")
                .password("qwer")
                .build());

        Rental rental = rentalRepository.save(Rental.builder()
                .product("장구")
                .count(1)
                .fromMember(fromMember)
                .toMember(toMember)
                .date(LocalDate.of(2000, 12, 20))
                .time(13)
                .build());

        RentalEditRequest request = RentalEditRequest.builder()
                .product("소고")
                .count(2)
                .fromMember(fromMember.getUsername())
                .date(LocalDate.of(2000, 12, 30))
                .time(15)
                .build();

        //when
        RentalNotFound e = assertThrows(RentalNotFound.class, () ->
                service.editRental(toMember.getId(), rental.getId() + 1, request));

        //then
        assertEquals(e.getMessage(), "존재하지 않는 대여입니다.");
        assertEquals(e.statusCode(), "404");
    }

    @Test
    @DisplayName("존재하지 않는 유저가 대여 수정 시도")
    void editFail2() {
        //given
        Member fromMember = memberRepository.save(Member.builder()
                .username("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        Member toMember = memberRepository.save(Member.builder()
                .username("윤호")
                .email("yoonH@naver.com")
                .password("qwer")
                .build());

        Rental rental = rentalRepository.save(Rental.builder()
                .product("장구")
                .count(1)
                .fromMember(fromMember)
                .toMember(toMember)
                .date(LocalDate.of(2000, 12, 20))
                .time(13)
                .build());

        RentalEditRequest request = RentalEditRequest.builder()
                .product("소고")
                .count(2)
                .fromMember(fromMember.getUsername())
                .date(LocalDate.of(2000, 12, 30))
                .time(15)
                .build();

        //when
        EditFailException e = assertThrows(EditFailException.class, () ->
                service.editRental(toMember.getId() + 1, rental.getId(), request));

        //then
        assertEquals(e.getMessage(), "수정할 수 없습니다.");
        assertEquals(e.statusCode(), "400");
    }

    @Test
    @DisplayName("대여자가 아닌 유저가 대여 수정 시도시 실패")
    void editFail3() {
        //given
        Member fromMember = memberRepository.save(Member.builder()
                .username("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        Member toMember = memberRepository.save(Member.builder()
                .username("윤호")
                .email("yoonH@naver.com")
                .password("qwer")
                .build());

        Member anotherMember = memberRepository.save(Member.builder()
                .username("아영")
                .email("young@hanmail.com")
                .password("asdf")
                .build());

        Rental rental = rentalRepository.save(Rental.builder()
                .product("장구")
                .count(1)
                .fromMember(fromMember)
                .toMember(toMember)
                .date(LocalDate.of(2000, 12, 20))
                .time(13)
                .build());

        RentalEditRequest request = RentalEditRequest.builder()
                .product("소고")
                .count(2)
                .fromMember(fromMember.getUsername())
                .date(LocalDate.of(2000, 12, 30))
                .time(15)
                .build();

        //when
        EditFailException e = assertThrows(EditFailException.class, () ->
                service.editRental(anotherMember.getId(), rental.getId(), request));

        //then
        assertEquals(e.getMessage(), "수정할 수 없습니다.");
        assertEquals(e.statusCode(), "400");
    }

    @Test
    @DisplayName("존재하지 않는 유저로 대여 수정 시도시 실패")
    void editFail4() {
        //given
        Member fromMember = memberRepository.save(Member.builder()
                .username("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        Member toMember = memberRepository.save(Member.builder()
                .username("윤호")
                .email("yoonH@naver.com")
                .password("qwer")
                .build());

        Rental rental = rentalRepository.save(Rental.builder()
                .product("장구")
                .count(1)
                .fromMember(fromMember)
                .toMember(toMember)
                .date(LocalDate.of(2000, 12, 20))
                .time(13)
                .build());

        RentalEditRequest request = RentalEditRequest.builder()
                .product("소고")
                .count(2)
                .fromMember("이상한 사람")
                .date(LocalDate.of(2000, 12, 30))
                .time(15)
                .build();

        //when
        MemberNotFound e = assertThrows(MemberNotFound.class, () ->
                service.editRental(toMember.getId(), rental.getId(), request));

        //then
        assertEquals(e.getMessage(), "존재하지 않는 유저입니다.");
        assertEquals(e.statusCode(), "404");
    }

    @Test
    @DisplayName("대여 삭제")
    void delete() {
        //given
        Member fromMember = memberRepository.save(Member.builder()
                .username("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        Member toMember = memberRepository.save(Member.builder()
                .username("윤호")
                .email("yoonH@naver.com")
                .password("qwer")
                .build());

        Rental rental = rentalRepository.save(Rental.builder()
                .product("장구")
                .count(1)
                .fromMember(fromMember)
                .toMember(toMember)
                .date(LocalDate.of(2000, 12, 20))
                .time(13)
                .build());

        //when
        service.deleteRental(toMember.getId(), rental.getId());

        //then
        assertEquals(rentalRepository.count(), 0);
    }

    @Test
    @DisplayName("존재하지 않는 대여 삭제")
    void deleteFail1() {
        Member fromMember = memberRepository.save(Member.builder()
                .username("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        Member toMember = memberRepository.save(Member.builder()
                .username("윤호")
                .email("yoonH@naver.com")
                .password("qwer")
                .build());

        Rental rental = rentalRepository.save(Rental.builder()
                .product("장구")
                .count(1)
                .fromMember(fromMember)
                .toMember(toMember)
                .date(LocalDate.of(2000, 12, 20))
                .time(13)
                .build());

        //when
        RentalNotFound e = assertThrows(RentalNotFound.class, () ->
                service.deleteRental(toMember.getId(), rental.getId() + 1));

        //then
        assertEquals(e.getMessage(), "존재하지 않는 대여입니다.");
        assertEquals(e.statusCode(), "404");
    }

    @Test
    @DisplayName("존재하지 않는 유저가 대여 삭제 시도")
    void deleteFail2() {
        Member fromMember = memberRepository.save(Member.builder()
                .username("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        Member toMember = memberRepository.save(Member.builder()
                .username("윤호")
                .email("yoonH@naver.com")
                .password("qwer")
                .build());

        Rental rental = rentalRepository.save(Rental.builder()
                .product("장구")
                .count(1)
                .fromMember(fromMember)
                .toMember(toMember)
                .date(LocalDate.of(2000, 12, 20))
                .time(13)
                .build());

        //when
        DeleteFailException e = assertThrows(DeleteFailException.class, () ->
                service.deleteRental(toMember.getId() + 1, rental.getId()));

        //then
        assertEquals(e.getMessage(), "삭제할 수 없습니다.");
        assertEquals(e.statusCode(), "400");
    }

    @Test
    @DisplayName("대여자가 아닌 유저가 대여 삭제 시도시 실패")
    void deleteFail3() {
        Member fromMember = memberRepository.save(Member.builder()
                .username("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        Member toMember = memberRepository.save(Member.builder()
                .username("윤호")
                .email("yoonH@naver.com")
                .password("qwer")
                .build());

        Member anotherMember = memberRepository.save(Member.builder()
                .username("아영")
                .email("young@hanmail.com")
                .password("asdf")
                .build());

        Rental rental = rentalRepository.save(Rental.builder()
                .product("장구")
                .count(1)
                .fromMember(fromMember)
                .toMember(toMember)
                .date(LocalDate.of(2000, 12, 20))
                .time(13)
                .build());

        //when
        DeleteFailException e = assertThrows(DeleteFailException.class, () ->
                service.deleteRental(anotherMember.getId(), rental.getId()));

        //then
        assertEquals(e.getMessage(), "삭제할 수 없습니다.");
        assertEquals(e.statusCode(), "400");
    }
}