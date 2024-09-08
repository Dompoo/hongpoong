package Dompoo.Hongpoong.api.controller.instrument;

import Dompoo.Hongpoong.api.dto.Instrument.*;
import Dompoo.Hongpoong.common.security.UserClaims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "4. 악기")
public interface InstrumentApi {
	
	@Operation(summary = "악기 추가")
	void createInstrument(UserClaims claims, InstrumentCreateRequest request);
	
	@Operation(summary = "다른 패의 악기 조회")
	List<InstrumentResponse> findAllOtherClubInstrument(UserClaims claims);
	
	@Operation(summary = "내 패의 악기 조회")
	List<InstrumentResponse> findAllMyClubInstrument(UserClaims claims);
	
	@Operation(summary = "악기 대여")
	InstrumentBorrowResponse borrowInstrument(UserClaims claims, InstrumentBorrowRequest request);
	
	@Operation(summary = "악기 반납")
	void returnInstrument(Long id);
	
	@Operation(summary = "악기 조회")
	InstrumentResponse findInstrumentDetail(Long id);
	
	@Operation(summary = "악기 수정")
	void editInstrument(UserClaims claims, Long id, InstrumentEditRequest request);
	
	@Operation(summary = "악기 삭제")
	void deleteInstrument(UserClaims claims, Long id);
}
