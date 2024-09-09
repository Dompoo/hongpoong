package Dompoo.Hongpoong.api.controller.common;

import Dompoo.Hongpoong.api.dto.common.request.SettingEditRequest;
import Dompoo.Hongpoong.api.dto.common.response.SettingResponse;
import Dompoo.Hongpoong.common.security.UserClaims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "7. 설정")
public interface CommonApi {
	
	@Operation(summary = "내 설정 확인")
	SettingResponse findMySetting(
			@Schema(hidden = true) UserClaims claims
	);
	
	@Operation(summary = "내 설정 수정")
	void editSetting(
			@Schema(hidden = true) UserClaims claims,
			@RequestBody SettingEditRequest request
	);
}
