package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.api.dto.reservation.request.ReservationCreateRequest;
import Dompoo.Hongpoong.api.dto.reservation.request.ReservationEditRequest;
import Dompoo.Hongpoong.api.dto.reservation.request.ReservationEndRequest;
import Dompoo.Hongpoong.api.dto.reservation.response.ReservationDetailResponse;
import Dompoo.Hongpoong.api.dto.reservation.response.ReservationEndResponse;
import Dompoo.Hongpoong.api.dto.reservation.response.ReservationResponse;
import Dompoo.Hongpoong.api.service.ReservationService;
import Dompoo.Hongpoong.common.exception.impl.*;
import Dompoo.Hongpoong.domain.jpaEntity.MemberJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.ReservationJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.ReservationEndImageJpaEntity;
import Dompoo.Hongpoong.domain.enums.Club;
import Dompoo.Hongpoong.domain.enums.ReservationTime;
import Dompoo.Hongpoong.domain.enums.ReservationType;
import Dompoo.Hongpoong.domain.enums.Role;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.AttendanceJpaRepository;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.MemberJpaRepository;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.ReservationEndImageJpaRepository;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.ReservationJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ReservationJpaEntityServiceTest {

    @Autowired
    private ReservationService service;
    @Autowired
    private ReservationJpaRepository reservationJpaRepository;
    @Autowired
    private MemberJpaRepository memberJpaRepository;
    @Autowired
    private AttendanceJpaRepository attendanceJpaRepository;
    @Autowired
    private ReservationEndImageJpaRepository reservationEndImageJpaRepository;
    
    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final LocalDate DATE = NOW.plusDays(10).toLocalDate();
    private static final LocalDate DATE2 = DATE.plusMonths(1);
    private static final ReservationType TYPE = ReservationType.FIXED_TIME;
    private static final ReservationTime START_TIME = ReservationTime.TIME_0900;
    private static final LocalTime START_TIME_LOCALTIME = START_TIME.localTime;
    private static final ReservationTime END_TIME = ReservationTime.TIME_1500;
    private static final LocalTime END_TIME_LOCALTIME = END_TIME.localTime;
    
    @BeforeEach
    void setUp() {
        reservationEndImageJpaRepository.deleteAllInBatch();
        attendanceJpaRepository.deleteAllInBatch();
        reservationJpaRepository.deleteAllInBatch();
        memberJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("예약 추가")
    void add() {
        //given
        MemberJpaEntity memberJpaEntity = memberJpaRepository.save(MemberJpaEntity.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .role(Role.LEADER)
                .club(Club.SANTLE)
                .build());

        ReservationCreateRequest request = ReservationCreateRequest.builder()
                .date(DATE)
                .type(TYPE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .message("")
                .build();

        //when
        service.createReservation(memberJpaEntity.getId(), request, NOW);

        //then
        assertEquals(1, reservationJpaRepository.count());
    }

    @Test
    @DisplayName("존재하지 않는 유저가 예약 추가 시도시 실패한다.")
    void addFail() {
        //given
        MemberJpaEntity memberJpaEntity = memberJpaRepository.save(MemberJpaEntity.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        ReservationCreateRequest request = ReservationCreateRequest.builder()
                .date(DATE)
                .type(TYPE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .message("")
                .build();

        //when
        MemberNotFound e = assertThrows(MemberNotFound.class,
                () -> service.createReservation(memberJpaEntity.getId() + 1, request, NOW));


        //then
        assertEquals(e.getMessage(), "존재하지 않는 유저입니다.");
        assertEquals(e.statusCode(), "404");
    }
    
    @Test
    @DisplayName("예약 추가시 시작시간이 종료시간보다 늦으면 안된다.")
    void addFail2() {
        //given
        MemberJpaEntity memberJpaEntity = memberJpaRepository.save(MemberJpaEntity.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());
        
        ReservationCreateRequest request = ReservationCreateRequest.builder()
                .date(DATE)
                .type(TYPE)
                .startTime(END_TIME)
                .endTime(START_TIME)
                .message("")
                .build();
        
        //when
        EndForwardStart e = assertThrows(EndForwardStart.class, () -> service.createReservation(memberJpaEntity.getId(), request, NOW));
        
        //then
        assertEquals(e.getMessage(), "시작 시간은 종료 시간보다 앞서야 합니다.");
        assertEquals(e.statusCode(), "400");
    }
    
    @Test
    @DisplayName("연도와 달로 예약 조회")
    void yearMonth() {
        //given
        MemberJpaEntity memberJpaEntity = memberJpaRepository.save(MemberJpaEntity.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());
        
        ReservationJpaEntity reservationJpaEntity = reservationJpaRepository.save(ReservationJpaEntity.builder()
                .creator(memberJpaEntity)
                .date(DATE)
                .type(TYPE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());
        
        reservationJpaRepository.save(ReservationJpaEntity.builder()
                .creator(memberJpaEntity)
                .date(DATE2)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());
        
        //when
        List<ReservationResponse> response = service.findAllReservationOfYearAndMonth(DATE.getYear(), DATE.getMonth().getValue());
        
        //then
        assertEquals(response.size(), 1);
        assertEquals(response.get(0).getReservationId(), reservationJpaEntity.getId());
        assertEquals(response.get(0).getCreatorName(), "창근");
        assertEquals(response.get(0).getDate(), DATE);
        assertEquals(response.get(0).getStartTime(), START_TIME_LOCALTIME);
        assertEquals(response.get(0).getEndTime(), END_TIME_LOCALTIME);
    }
    
    @Test
    @DisplayName("날짜로 예약 조회")
    void day() {
        //given
        MemberJpaEntity memberJpaEntity = memberJpaRepository.save(MemberJpaEntity.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());
        
        ReservationJpaEntity reservationJpaEntity = reservationJpaRepository.save(ReservationJpaEntity.builder()
                .creator(memberJpaEntity)
                .date(DATE)
                .type(TYPE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());
        
        reservationJpaRepository.save(ReservationJpaEntity.builder()
                .creator(memberJpaEntity)
                .date(DATE2)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());
        
        //when
        List<ReservationResponse> response = service.findAllReservationOfDate(DATE);
        
        //then
        assertEquals(response.size(), 1);
        assertEquals(response.get(0).getReservationId(), reservationJpaEntity.getId());
        assertEquals(response.get(0).getCreatorName(), "창근");
        assertEquals(response.get(0).getDate(), DATE);
        assertEquals(response.get(0).getStartTime(), START_TIME_LOCALTIME);
        assertEquals(response.get(0).getEndTime(), END_TIME_LOCALTIME);
    }
    
    @Test
    @DisplayName("오늘 해야하는 예약 전체 조회")
    void todo() {
        //given
        MemberJpaEntity memberJpaEntity = memberJpaRepository.save(MemberJpaEntity.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());
        
        ReservationJpaEntity reservationJpaEntity = reservationJpaRepository.save(ReservationJpaEntity.builder()
                .creator(memberJpaEntity)
                .date(DATE)
                .type(TYPE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());
        
        reservationJpaRepository.save(ReservationJpaEntity.builder()
                .creator(memberJpaEntity)
                .date(DATE2)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());
        
        //when
        List<ReservationResponse> response = service.findAllReservationOfDate(DATE);
        
        //then
        assertEquals(response.size(), 1);
        assertEquals(response.get(0).getReservationId(), reservationJpaEntity.getId());
        assertEquals(response.get(0).getCreatorName(), "창근");
        assertEquals(response.get(0).getDate(), DATE);
        assertEquals(response.get(0).getStartTime(), START_TIME_LOCALTIME);
        assertEquals(response.get(0).getEndTime(), END_TIME_LOCALTIME);
    }

    @Test
    @DisplayName("예약 상세 조회")
    void detail() {
        //given
        MemberJpaEntity memberJpaEntity = memberJpaRepository.save(MemberJpaEntity.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        ReservationJpaEntity reservationJpaEntity = reservationJpaRepository.save(ReservationJpaEntity.builder()
                .creator(memberJpaEntity)
                .date(DATE)
                .type(TYPE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());

        //when
        ReservationDetailResponse response = service.findReservationDetail(reservationJpaEntity.getId());
        
        //then
        assertEquals(response.getReservationId(), reservationJpaEntity.getId());
        assertEquals(response.getCreatorName(), "창근");
        assertEquals(response.getDate(), DATE);
        assertEquals(response.getStartTime(), START_TIME_LOCALTIME);
        assertEquals(response.getEndTime(), END_TIME_LOCALTIME);
    }

    @Test
    @DisplayName("존재하지 않는 예약 상세 조회")
    void detailFail() {
        //given
        MemberJpaEntity memberJpaEntity = memberJpaRepository.save(MemberJpaEntity.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        ReservationJpaEntity reservationJpaEntity = reservationJpaRepository.save(ReservationJpaEntity.builder()
                .creator(memberJpaEntity)
                .date(DATE)
                .type(TYPE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());

        //when
        ReservationNotFound e = assertThrows(ReservationNotFound.class,
                () -> service.findReservationDetail(reservationJpaEntity.getId() + 1));

        //then
        assertEquals(e.getMessage(), "존재하지 않는 예약입니다.");
        assertEquals(e.statusCode(), "404");
    }
    
    @Test
    @DisplayName("예약 시간 연장")
    void extend() {
        //given
        MemberJpaEntity memberJpaEntity = memberJpaRepository.save(MemberJpaEntity.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());
        
        ReservationJpaEntity reservationJpaEntity = reservationJpaRepository.save(ReservationJpaEntity.builder()
                .creator(memberJpaEntity)
                .date(DATE)
                .type(TYPE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());
        
        LocalTime now = END_TIME_LOCALTIME.minusMinutes(25);
        
        //when
        service.extendReservationTime(memberJpaEntity.getId(), reservationJpaEntity.getId(), now);
        
        //then
        ReservationJpaEntity editedReservationJpaEntity = reservationJpaRepository.findById(reservationJpaEntity.getId()).get();
        assertEquals(ReservationTime.from(END_TIME.localTime.plusMinutes(30)), editedReservationJpaEntity.getEndTime());
    }
    
    @Test
    @DisplayName("예약 시간 연장은 예약자만 가능하다.")
    void extendFail3() {
        //given
        MemberJpaEntity memberJpaEntity = memberJpaRepository.save(MemberJpaEntity.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());
        
        MemberJpaEntity anotherMemberJpaEntity = memberJpaRepository.save(MemberJpaEntity.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());
        
        ReservationJpaEntity reservationJpaEntity = reservationJpaRepository.save(ReservationJpaEntity.builder()
                .creator(memberJpaEntity)
                .date(DATE)
                .type(TYPE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());
        
        LocalTime now = END_TIME_LOCALTIME.minusMinutes(35);
        
        //when
        EditFailException e = assertThrows(EditFailException.class, () -> service.extendReservationTime(anotherMemberJpaEntity.getId(), reservationJpaEntity.getId(), now));
        
        //then
        assertEquals("403", e.statusCode());
        assertEquals("수정할 수 없습니다.", e.getMessage());
    }
    
    @Test
    @DisplayName("예약 시간 연장은 종료 30분 전에만 가능하다.")
    void extendFail1() {
        //given
        MemberJpaEntity memberJpaEntity = memberJpaRepository.save(MemberJpaEntity.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());
        
        ReservationJpaEntity reservationJpaEntity = reservationJpaRepository.save(ReservationJpaEntity.builder()
                .creator(memberJpaEntity)
                .date(DATE)
                .type(TYPE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());
        
        LocalTime now = END_TIME_LOCALTIME.minusMinutes(35);
        
        //when
        TimeExtendNotAvailableException e = assertThrows(TimeExtendNotAvailableException.class, () -> service.extendReservationTime(memberJpaEntity.getId(), reservationJpaEntity.getId(), now));
        
        //then
        assertEquals("400", e.statusCode());
        assertEquals("시간 연장은 연습 종료 30분 전에만 가능합니다.", e.getMessage());
    }
    
    @Test
    @DisplayName("예약 시간 연장은 연습 종료 후에 불가능하다.")
    void extendFail2() {
        //given
        MemberJpaEntity memberJpaEntity = memberJpaRepository.save(MemberJpaEntity.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());
        
        ReservationJpaEntity reservationJpaEntity = reservationJpaRepository.save(ReservationJpaEntity.builder()
                .creator(memberJpaEntity)
                .date(DATE)
                .type(TYPE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());
        
        LocalTime now = END_TIME_LOCALTIME.plusMinutes(1);
        
        //when
        TimeExtendNotAvailableException e = assertThrows(TimeExtendNotAvailableException.class, () -> service.extendReservationTime(memberJpaEntity.getId(), reservationJpaEntity.getId(), now));
        
        //then
        assertEquals("400", e.statusCode());
        assertEquals("시간 연장은 연습 종료 30분 전에만 가능합니다.", e.getMessage());
    }
    
    @Test
    @DisplayName("예약 종료")
    void end() {
        //given
        MemberJpaEntity memberJpaEntity = memberJpaRepository.save(MemberJpaEntity.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());
        
        ReservationJpaEntity reservationJpaEntity = reservationJpaRepository.save(ReservationJpaEntity.builder()
                .creator(memberJpaEntity)
                .date(DATE)
                .type(TYPE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());
        
        ReservationEndRequest request = ReservationEndRequest.builder()
                .endImages(List.of("image1", "image2"))
                .build();
        
        //when
        service.endReservation(memberJpaEntity.getId(), reservationJpaEntity.getId(), request);
        
        //then
        List<ReservationEndImageJpaEntity> images = reservationEndImageJpaRepository.findAllByReservation(reservationJpaEntity);
        assertTrue(images.stream().map(ReservationEndImageJpaEntity::getImageUrl).toList().containsAll(List.of("image1", "image2")));
    }
    
    @Test
    @DisplayName("예약 종료 후 상세 정보 확인")
    void endDetail() {
        //given
        MemberJpaEntity memberJpaEntity = memberJpaRepository.save(MemberJpaEntity.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());
        
        ReservationJpaEntity reservationJpaEntity = reservationJpaRepository.save(ReservationJpaEntity.builder()
                .creator(memberJpaEntity)
                .date(DATE)
                .type(TYPE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());
        
        reservationEndImageJpaRepository.saveAll(List.of(
                ReservationEndImageJpaEntity.builder().reservationJpaEntity(reservationJpaEntity).imageUrl("image1").build(),
                ReservationEndImageJpaEntity.builder().reservationJpaEntity(reservationJpaEntity).imageUrl("image2").build()
        ));
        
        //when
        ReservationEndResponse reservationEndDetail = service.findReservationEndDetail(reservationJpaEntity.getId());
        
        //then
        assertEquals("창근", reservationEndDetail.getCreatorName());
        assertEquals("dompoo@gmail.com", reservationEndDetail.getEmail());
        assertTrue(reservationEndDetail.getImages().containsAll(List.of("image1", "image2")));
    }

    @Test
    @DisplayName("예약 수정")
    void edit() {
        //given
        MemberJpaEntity memberJpaEntity = memberJpaRepository.save(MemberJpaEntity.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        ReservationJpaEntity reservationJpaEntity = reservationJpaRepository.save(ReservationJpaEntity.builder()
                .creator(memberJpaEntity)
                .date(DATE)
                .type(TYPE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());

        ReservationEditRequest request = ReservationEditRequest.builder()
                .date(LocalDate.of(2025, 12, 15))
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build();
        
        LocalDateTime now = LocalDateTime.of(2000, 5, 17, 11, 23, 30);

        //when
        service.editReservation(memberJpaEntity.getId(), reservationJpaEntity.getId(), request.toDto(), now);

        //then
        ReservationJpaEntity find = assertDoesNotThrow(() -> reservationJpaRepository.findById(reservationJpaEntity.getId())
                .orElseThrow());
        assertEquals(find.getCreator().getId(), memberJpaEntity.getId());
        assertEquals(find.getDate(), LocalDate.of(2025, 12, 15));
        assertEquals(find.getStartTime(), START_TIME);
        assertEquals(find.getEndTime(), END_TIME);
    }

    @Test
    @DisplayName("예약 수정 실패")
    void editFail() {
        //given
        MemberJpaEntity memberJpaEntity = memberJpaRepository.save(MemberJpaEntity.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        ReservationJpaEntity reservationJpaEntity = reservationJpaRepository.save(ReservationJpaEntity.builder()
                .creator(memberJpaEntity)
                .date(DATE)
                .type(TYPE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());

        ReservationEditRequest request = ReservationEditRequest.builder()
                .date(LocalDate.of(2025, 12, 15))
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build();
        
        LocalDateTime now = LocalDateTime.of(2000, 5, 17, 11, 23, 30);

        //when
        ReservationNotFound e = assertThrows(ReservationNotFound.class, () ->
                service.editReservation(memberJpaEntity.getId(), reservationJpaEntity.getId() + 1, request.toDto(), now));

        //then
        assertEquals(e.getMessage(), "존재하지 않는 예약입니다.");
        assertEquals(e.statusCode(), "404");
    }

    @Test
    @DisplayName("예약자가 아닌 유저가 예약 수정 시도시 실패")
    void editFai2() {
        //given
        MemberJpaEntity memberJpaEntity = memberJpaRepository.save(MemberJpaEntity.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        ReservationJpaEntity reservationJpaEntity = reservationJpaRepository.save(ReservationJpaEntity.builder()
                .creator(memberJpaEntity)
                .date(DATE)
                .type(TYPE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());

        ReservationEditRequest request = ReservationEditRequest.builder()
                .date(LocalDate.of(2025, 12, 15))
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build();
        
        LocalDateTime now = LocalDateTime.of(2000, 5, 17, 11, 23, 30);

        //when
        EditFailException e = assertThrows(EditFailException.class, () ->
                service.editReservation(memberJpaEntity.getId() + 1, reservationJpaEntity.getId(), request.toDto(), now));

        //then
        assertEquals(e.getMessage(), "수정할 수 없습니다.");
        assertEquals(e.statusCode(), "403");
    }

    @Test
    @DisplayName("예약 삭제")
    void delete() {
        //given
        MemberJpaEntity memberJpaEntity = memberJpaRepository.save(MemberJpaEntity.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        ReservationJpaEntity reservationJpaEntity = reservationJpaRepository.save(ReservationJpaEntity.builder()
                .creator(memberJpaEntity)
                .date(DATE)
                .type(TYPE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());

        //when
        service.deleteReservation(memberJpaEntity.getId(), reservationJpaEntity.getId());

        //then
        assertEquals(reservationJpaRepository.count(), 0);
    }

    @Test
    @DisplayName("예약 삭제 실패")
    void deleteFail() {
        //given
        MemberJpaEntity memberJpaEntity = memberJpaRepository.save(MemberJpaEntity.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        ReservationJpaEntity reservationJpaEntity = reservationJpaRepository.save(ReservationJpaEntity.builder()
                .creator(memberJpaEntity)
                .date(DATE)
                .type(TYPE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());

        //when
        ReservationNotFound e = assertThrows(ReservationNotFound.class, () ->
                service.deleteReservation(memberJpaEntity.getId(), reservationJpaEntity.getId() + 1));

        //then
        assertEquals(e.getMessage(), "존재하지 않는 예약입니다.");
        assertEquals(e.statusCode(), "404");
    }

    @Test
    @DisplayName("예약자가 아닌 유저가 예약 삭제 시도시 실패")
    void deleteFail2() {
        //given
        MemberJpaEntity memberJpaEntity = memberJpaRepository.save(MemberJpaEntity.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        ReservationJpaEntity reservationJpaEntity = reservationJpaRepository.save(ReservationJpaEntity.builder()
                .creator(memberJpaEntity)
                .date(DATE)
                .type(TYPE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());

        //when
        DeleteFailException e = assertThrows(DeleteFailException.class, () ->
                service.deleteReservation(memberJpaEntity.getId() + 1, reservationJpaEntity.getId()));

        //then
        assertEquals(e.getMessage(), "삭제할 수 없습니다.");
        assertEquals(e.statusCode(), "403");
    }

    @Test
    @DisplayName("관리자 예약 수정")
    void editByAdmin() {
        //given
        MemberJpaEntity memberJpaEntity = memberJpaRepository.save(MemberJpaEntity.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());

        ReservationJpaEntity reservationJpaEntity = reservationJpaRepository.save(ReservationJpaEntity.builder()
                .creator(memberJpaEntity)
                .date(DATE)
                .type(TYPE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());

        ReservationEditRequest request = ReservationEditRequest.builder()
                .date(LocalDate.of(2025, 12, 15))
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build();
        
        LocalDateTime now = LocalDateTime.of(2000, 5, 17, 11, 23, 30);

        //when
        service.editReservationByAdmin(reservationJpaEntity.getId(), request.toDto(), now);

        //then
        ReservationJpaEntity find = assertDoesNotThrow(() -> reservationJpaRepository.findById(reservationJpaEntity.getId())
                .orElseThrow());
        assertEquals(find.getCreator().getId(), memberJpaEntity.getId());
        assertEquals(find.getDate(), LocalDate.of(2025, 12, 15));
        assertEquals(find.getStartTime(), START_TIME);
        assertEquals(find.getEndTime(), END_TIME);
    }
    
    @Test
    @DisplayName("관리자 예약 삭제")
    void deleteByAdmin() {
        //given
        MemberJpaEntity memberJpaEntity = memberJpaRepository.save(MemberJpaEntity.builder()
                .name("창근")
                .email("dompoo@gmail.com")
                .password("1234")
                .build());
        
        ReservationJpaEntity reservationJpaEntity = reservationJpaRepository.save(ReservationJpaEntity.builder()
                .creator(memberJpaEntity)
                .date(DATE)
                .type(TYPE)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .build());
        
        //when
        service.deleteReservationByAdmin(reservationJpaEntity.getId());
        
        //then
        assertEquals(reservationJpaRepository.count(), 0);
    }
}