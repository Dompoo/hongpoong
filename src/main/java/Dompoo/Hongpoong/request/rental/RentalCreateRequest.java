//package Dompoo.Hongpoong.request.rental;
//
//import jakarta.validation.constraints.NotNull;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.util.List;
//
//@Getter
//@Setter
//@NoArgsConstructor
///*
//RequestBody
//{
//    "product": "장구",
//    "count": 1,
//    "fromMember": "산틀",
//    "date": "2024-04-18",
//    "time": 13
//}
// */
//public class RentalCreateRequest {
//
//    @NotNull(message = "대여할 악기는 비어있을 수 없습니다.")
//    private List<Long> instrumentIds;
//
//    @NotNull(message = "예약은 비어있을 수 없습니다.")
//    private Long reservationId;
//
//    @Builder
//    public RentalCreateRequest(List<Long> instrumentIds, Long reservationId) {
//        this.instrumentIds = instrumentIds;
//        this.reservationId = reservationId;
//    }
//}
