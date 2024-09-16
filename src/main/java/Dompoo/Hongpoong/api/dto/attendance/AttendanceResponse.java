package Dompoo.Hongpoong.api.dto.attendance;

import Dompoo.Hongpoong.api.dto.member.response.MemberResponse;
import Dompoo.Hongpoong.domain.entity.Attendance;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.enums.AttendanceStatus;
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
    
    public static AttendanceResponse from(Member member, AttendanceStatus attendanceStatus) {
        return AttendanceResponse.builder()
                .member(MemberResponse.from(member))
                .attendance(attendanceStatus.korName)
                .build();
    }
    
    public static AttendanceResponse from(Attendance attendance) {
        return AttendanceResponse.builder()
                .member(MemberResponse.from(attendance.getMember()))
                .attendance(attendance.getAttendanceStatus().korName)
                .build();
    }
    
    public static List<AttendanceResponse> fromList(List<Attendance> participateList) {
        return participateList.stream().map(AttendanceResponse::from).toList();
    }
}
