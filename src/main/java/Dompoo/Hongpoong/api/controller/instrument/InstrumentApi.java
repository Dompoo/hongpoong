package Dompoo.Hongpoong.api.controller.instrument;

import Dompoo.Hongpoong.api.dto.Instrument.*;
import Dompoo.Hongpoong.common.security.UserClaims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "4. 악기")
public interface InstrumentApi {
	
	@Operation(summary = "악기 추가")
	void createInstrument(
			@Schema(hidden = true) UserClaims claims,
			@RequestBody InstrumentCreateRequest request
	);
	
	@Operation(summary = "다른 패의 악기 조회")
	List<InstrumentResponse> findAllOtherClubInstrument(
			@Schema(hidden = true) UserClaims claims
	);
	
	@Operation(summary = "내 패의 악기 조회")
	List<InstrumentResponse> findAllMyClubInstrument(
			@Schema(hidden = true) UserClaims claims
	);
	
	@Operation(summary = "악기 대여")
	InstrumentBorrowResponse borrowInstrument(
			@Schema(hidden = true) UserClaims claims,
			@RequestBody InstrumentBorrowRequest request);
	
	@Operation(summary = "악기 반납")
	void returnInstrument(
			@Parameter(description = "악기 id") Long instrumentId
	);
	
	@Operation(summary = "악기 조회")
	InstrumentResponse findInstrumentDetail(
			@Parameter(description = "악기 id") Long instrumentId
	);
	
	@Operation(summary = "악기 수정")
	void editInstrument(
			@Schema(hidden = true) UserClaims claims,
			@Parameter(description = "악기 id") Long instrumentId,
			@RequestBody InstrumentEditRequest request
	);
	
	@Operation(summary = "악기 삭제")
	void deleteInstrument(
			@Schema(hidden = true) UserClaims claims,
			@Parameter(description = "악기 id") Long instrumentId
	);
}
