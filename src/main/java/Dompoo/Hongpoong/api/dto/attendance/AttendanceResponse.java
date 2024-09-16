package Dompoo.Hongpoong.api.dto.attendance;

import Dompoo.Hongpoong.api.dto.member.response.MemberResponse;
import Dompoo.Hongpoong.domain.enums.AttendanceStatus;
import Dompoo.Hongpoong.domain.jpaEntity.AttendanceJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.MemberJpaEntity;
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
    
    public static AttendanceResponse from(MemberJpaEntity memberJpaEntity, AttendanceStatus attendanceStatus) {
        return AttendanceResponse.builder()
                .member(MemberResponse.from(memberJpaEntity))
                .attendance(attendanceStatus.korName)
                .build();
    }
    
    public static AttendanceResponse from(AttendanceJpaEntity attendanceJpaEntity) {
        return AttendanceResponse.builder()
                .member(MemberResponse.from(attendanceJpaEntity.getMember()))
                .attendance(attendanceJpaEntity.getAttendanceStatus().korName)
                .build();
    }
    
    public static List<AttendanceResponse> fromList(List<AttendanceJpaEntity> participateList) {
        return participateList.stream().map(AttendanceResponse::from).toList();
    }
}
