package Dompoo.Hongpoong.api.controller.instrument;

import Dompoo.Hongpoong.api.dto.Instrument.request.InstrumentBorrowRequest;
import Dompoo.Hongpoong.api.dto.Instrument.request.InstrumentCreateRequest;
import Dompoo.Hongpoong.api.dto.Instrument.request.InstrumentEditRequest;
import Dompoo.Hongpoong.api.dto.Instrument.response.InstrumentDetailResponse;
import Dompoo.Hongpoong.api.dto.Instrument.response.InstrumentResponse;
import Dompoo.Hongpoong.common.security.UserClaims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "4. 악기")
public interface InstrumentApi {
	
	@Operation(summary = "[의장/패짱상쇠/수악기] 악기 추가")
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
	
	@Operation(summary = "악기 상세 조회")
	InstrumentDetailResponse findInstrumentDetail(
			@Parameter(description = "악기 id") Long instrumentId
	);
	
	@Operation(summary = "악기 대여")
	void borrowInstrument(
			@Schema(hidden = true) UserClaims claims,
			@Parameter(description = "악기 id") Long instrumentId,
			@RequestBody InstrumentBorrowRequest request
	);
	
	@Operation(summary = "악기 반납")
	void returnInstrument(
			@Schema(hidden = true) UserClaims claims,
			@Parameter(description = "악기 id") Long instrumentId
	);
	
	@Operation(summary = "[패짱상쇠/수악기] 내 패의 악기 수정")
	void editInstrument(
			@Schema(hidden = true) UserClaims claims,
			@Parameter(description = "악기 id") Long instrumentId,
			@RequestBody InstrumentEditRequest request
	);
	
	@Operation(summary = "[패짱상쇠/수악기] 내 패의 악기 삭제")
	void deleteInstrument(
			@Schema(hidden = true) UserClaims claims,
			@Parameter(description = "악기 id") Long instrumentId
	);
	
	@Operation(summary = "[의장] 악기 수정")
	void editInstrumentByAdmin(
			@Parameter(description = "악기 id") Long instrumentId,
			@RequestBody InstrumentEditRequest request
	);
	
	@Operation(summary = "[의장] 악기 삭제")
	void deleteInstrumentByAdmin(
			@Parameter(description = "악기 id") Long instrumentId
	);
}
