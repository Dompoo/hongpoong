package Dompoo.Hongpoong.api.controller.attendance;

import Dompoo.Hongpoong.api.dto.attendance.AttendanceResponse;
import Dompoo.Hongpoong.api.service.AttendanceService;
import Dompoo.Hongpoong.common.security.UserClaims;
import Dompoo.Hongpoong.common.security.annotation.LoginUser;
import Dompoo.Hongpoong.common.security.annotation.Secured;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/attendance")
public class AttendanceController implements AttendanceApi {
	
	private final AttendanceService attendanceService;
	
	@Secured
	@GetMapping("/{reservationId}")
	public List<AttendanceResponse> findAttendance(@PathVariable Long reservationId) {
		return attendanceService.findAttendance(reservationId);
	}
	
	@Secured
	@PostMapping("/{reservationId}/attendance")
	public AttendanceResponse attendReservation(@LoginUser UserClaims userClaims, @PathVariable Long reservationId) {
		return attendanceService.attendReservation(userClaims.getId(), reservationId, LocalDateTime.now());
	}
	
	@Secured
	@PostMapping("/{reservationId}/close")
	public List<AttendanceResponse> clostAttendance(@LoginUser UserClaims userClaims, @PathVariable Long reservationId) {
		return attendanceService.closeAttendance(userClaims.getId(), reservationId);
	}
}
