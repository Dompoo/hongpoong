package Dompoo.Hongpoong.api.dto.attendance;

import Dompoo.Hongpoong.domain.domain.Attendance;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AttendanceResultResponse {
    
    @Schema(example = "출석")
    private final String attendance;
    
    public static AttendanceResultResponse from(Attendance attendance) {
        return AttendanceResultResponse.builder()
                .attendance(attendance.getAttendanceStatus().korName)
                .build();
    }
}
