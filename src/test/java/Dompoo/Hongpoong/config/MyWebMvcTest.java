package Dompoo.Hongpoong.config;

import Dompoo.Hongpoong.api.config.WebMvcConfig;
import Dompoo.Hongpoong.api.controller.*;
import Dompoo.Hongpoong.api.service.*;
import Dompoo.Hongpoong.common.security.AuthInterceptor;
import Dompoo.Hongpoong.common.security.JwtProvider;
import Dompoo.Hongpoong.common.security.LoginUserArgumentResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
		value = {
				AuthController.class,
				ChatController.class,
				CommonController.class,
				InfoController.class,
				InstrumentController.class,
				MemberController.class,
				ReservationController.class,
		},
		excludeFilters = @ComponentScan.Filter(
				type = FilterType.ASSIGNABLE_TYPE,
				classes = {
						AuthInterceptor.class,
						LoginUserArgumentResolver.class,
						JwtProvider.class,
						WebMvcConfig.class,
				}
		)
)
@Import(TestWebMvcConfig.class)
@ActiveProfiles("test")
public abstract class MyWebMvcTest {
	@Autowired
	protected ObjectMapper objectMapper;
	@Autowired
	protected MockMvc mockMvc;
	@MockBean
	protected AuthService authService;
	@MockBean
	protected ChatService chatService;
	@MockBean
	protected CommonService commonService;
	@MockBean
	protected InfoService infoService;
	@MockBean
	protected InstrumentService instrumentService;
	@MockBean
	protected MemberService memberService;
	@MockBean
	protected ReservationService reservationService;
	@MockBean
	protected AttendanceService attendanceService;
}
