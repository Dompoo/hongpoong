package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.domain.Rental;
import Dompoo.Hongpoong.exception.RentalNotFound;
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
    private RentalRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("예약 리스트 조회")
    void list() {
        //given
        Rental rental1 = repository.save(Rental.builder()
                .product("장구")
                .count(1)
                .fromMember("악반")
                .toMember("산틀")
                .date(LocalDate.of(2000, 12, 20))
                .time(13)
                .build());

        Rental rental2 = repository.save(Rental.builder()
                .product("소고")
                .count(4)
                .fromMember("화랑")
                .toMember("들녘")
                .date(LocalDate.of(2000, 12, 30))
                .time(15)
                .build());

        //when
        List<RentalResponse> list = service.getList();

        //then
        assertEquals(repository.count(), 2);
        assertEquals(list.getFirst().getId(), rental1.getId());
        assertEquals(list.getFirst().getProduct(), "장구");
        assertEquals(list.getFirst().getCount(), 1);
        assertEquals(list.getFirst().getFromMember(), "악반");
        assertEquals(list.getFirst().getToMember(), "산틀");
        assertEquals(list.get(0).getDate(), LocalDate.of(2000, 12, 20));
        assertEquals(list.get(0).getTime(), 13);
        assertEquals(list.get(1).getId(), rental2.getId());
        assertEquals(list.get(1).getProduct(), "소고");
        assertEquals(list.get(1).getCount(), 4);
        assertEquals(list.get(1).getFromMember(), "화랑");
        assertEquals(list.get(1).getToMember(), "들녘");
        assertEquals(list.get(1).getDate(), LocalDate.of(2000, 12, 30));
        assertEquals(list.get(1).getTime(), 15);
    }

    @Test
    @DisplayName("예약 추가")
    void addOne() {
        //given
        RentalCreateRequest request = RentalCreateRequest.builder()
                .product("장구")
                .count(1)
                .fromMember("악반")
                .toMember("산틀")
                .date(LocalDate.of(2000, 12, 20))
                .time(13)
                .build();

        //when
        service.addRental(request);

        //then
        assertEquals(repository.count(), 1);
        assertEquals(repository.findAll().getFirst().getProduct(), "장구");
        assertEquals(repository.findAll().getFirst().getCount(), 1);
        assertEquals(repository.findAll().getFirst().getFromMember(), "악반");
        assertEquals(repository.findAll().getFirst().getToMember(), "산틀");
        assertEquals(repository.findAll().getFirst().getDate(), LocalDate.of(2000, 12, 20));
        assertEquals(repository.findAll().getFirst().getTime(), 13);
    }

    @Test
    @DisplayName("예약 상세 조회")
    void getOne() {
        //given
        Rental rental = repository.save(Rental.builder()
                .product("장구")
                .count(1)
                .fromMember("악반")
                .toMember("산틀")
                .date(LocalDate.of(2000, 12, 20))
                .time(13)
                .build());

        //when
        RentalResponse response = service.getDetail(rental.getId());

        //then
        assertEquals(response.getId(), rental.getId());
        assertEquals(response.getProduct(), "장구");
        assertEquals(response.getCount(), 1);
        assertEquals(response.getFromMember(), "악반");
        assertEquals(response.getToMember(), "산틀");
        assertEquals(response.getDate(), LocalDate.of(2000, 12, 20));
        assertEquals(response.getTime(), 13);
    }

    @Test
    @DisplayName("존재하지 않는 예약 상세 조회")
    void getOneFail() {
        Rental rental = repository.save(Rental.builder()
                .product("장구")
                .count(1)
                .fromMember("악반")
                .toMember("산틀")
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
        Rental rental = repository.save(Rental.builder()
                .product("장구")
                .count(1)
                .fromMember("악반")
                .toMember("산틀")
                .date(LocalDate.of(2000, 12, 20))
                .time(13)
                .build());

        RentalEditRequest request = RentalEditRequest.builder()
                .product("소고")
                .count(2)
                .fromMember("화랑")
                .date(LocalDate.of(2000, 12, 30))
                .time(15)
                .build();

        //when
        service.editRental(rental.getId(), request);

        //then
        Rental find = assertDoesNotThrow(() -> repository.findById(rental.getId())
                .orElseThrow());
        assertEquals(find.getProduct(), "소고");
        assertEquals(find.getCount(), 2);
        assertEquals(find.getFromMember(), "화랑");
        assertEquals(find.getToMember(), "산틀");
        assertEquals(find.getDate(), LocalDate.of(2000, 12, 30));
        assertEquals(find.getTime(), 15);
    }

    @Test
    @DisplayName("대여 일부 수정")
    void edit2() {
        //given
        Rental rental = repository.save(Rental.builder()
                .product("장구")
                .count(1)
                .fromMember("악반")
                .toMember("산틀")
                .date(LocalDate.of(2000, 12, 20))
                .time(13)
                .build());

        RentalEditRequest request = RentalEditRequest.builder()
                .product("소고")
                .count(2)
                .build();

        //when
        service.editRental(rental.getId(), request);

        //then
        Rental find = assertDoesNotThrow(() -> repository.findById(rental.getId())
                .orElseThrow());
        assertEquals(find.getProduct(), "소고");
        assertEquals(find.getCount(), 2);
        assertEquals(find.getFromMember(), "악반");
        assertEquals(find.getToMember(), "산틀");
        assertEquals(find.getDate(), LocalDate.of(2000, 12, 20));
        assertEquals(find.getTime(), 13);
    }

    @Test
    @DisplayName("대여 수정 실패")
    void editFail() {
        //given
        Rental rental = repository.save(Rental.builder()
                .product("장구")
                .count(1)
                .fromMember("악반")
                .toMember("산틀")
                .date(LocalDate.of(2000, 12, 20))
                .time(13)
                .build());

        RentalEditRequest request = RentalEditRequest.builder()
                .product("소고")
                .count(2)
                .fromMember("화랑")
                .date(LocalDate.of(2000, 12, 30))
                .time(15)
                .build();

        //when
        RentalNotFound e = assertThrows(RentalNotFound.class, () ->
                service.editRental(rental.getId() + 1, request));

        //then
        assertEquals(e.getMessage(), "존재하지 않는 대여입니다.");
        assertEquals(e.statusCode(), "404");
    }

    @Test
    @DisplayName("대여 삭제")
    void delete() {
        //given
        Rental rental = repository.save(Rental.builder()
                .product("장구")
                .count(1)
                .fromMember("악반")
                .toMember("산틀")
                .date(LocalDate.of(2000, 12, 20))
                .time(13)
                .build());

        //when
        service.deleteRental(rental.getId());

        //then
        assertEquals(repository.count(), 0);
    }

    @Test
    @DisplayName("존재하지 않는 대여 삭제")
    void deleteFail() {
        Rental rental = repository.save(Rental.builder()
                .product("장구")
                .count(1)
                .fromMember("악반")
                .toMember("산틀")
                .date(LocalDate.of(2000, 12, 20))
                .time(13)
                .build());

        //when
        RentalNotFound e = assertThrows(RentalNotFound.class, () ->
                service.deleteRental(rental.getId() + 1));

        //then
        assertEquals(e.getMessage(), "존재하지 않는 대여입니다.");
        assertEquals(e.statusCode(), "404");
    }
}