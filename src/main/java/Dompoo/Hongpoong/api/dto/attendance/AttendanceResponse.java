package Dompoo.Hongpoong.api.dto.attendance;

import Dompoo.Hongpoong.api.dto.member.response.MemberResponse;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.entity.ReservationParticipate;
import Dompoo.Hongpoong.domain.enums.Attendance;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AttendanceResponse {
    
    private final MemberResponse member;
    
    @Schema(example = "출석")
    private final String attendance;
    
    public static AttendanceResponse from(Member member, Attendance attendance) {
        return AttendanceResponse.builder()
                .member(MemberResponse.from(member))
                .attendance(attendance.korName)
                .build();
    }
    
    public static AttendanceResponse from(ReservationParticipate reservationParticipate) {
        return AttendanceResponse.builder()
                .member(MemberResponse.from(reservationParticipate.getMember()))
                .attendance(reservationParticipate.getAttendance().korName)
                .build();
    }
    
    public static List<AttendanceResponse> fromList(List<ReservationParticipate> participateList) {
        return participateList.stream().map(AttendanceResponse::from).toList();
    }
}
