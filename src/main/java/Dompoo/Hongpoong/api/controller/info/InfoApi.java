package Dompoo.Hongpoong.api.controller.info;

import Dompoo.Hongpoong.api.dto.info.request.InfoCreateRequest;
import Dompoo.Hongpoong.api.dto.info.request.InfoEditRequest;
import Dompoo.Hongpoong.api.dto.info.response.InfoDetailResponse;
import Dompoo.Hongpoong.api.dto.info.response.InfoResponse;
import Dompoo.Hongpoong.common.security.UserClaims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "6. 공지사항")
public interface InfoApi {
	
	@Operation(summary = "[관리자/패짱] 공지사항 작성")
	void createInfo(
			@Schema(hidden = true) UserClaims claims,
			@RequestBody InfoCreateRequest request
	);
	
	@Operation(summary = "공지사항 전체 조회")
	List<InfoResponse> findAllInfo();
	
	@Operation(summary = "공지사항 상세 조회")
	InfoDetailResponse findInfoDetail(
			@Parameter(description = "공지사항 id") Long infoId
	);
	
	@Operation(summary = "[패짱] 공지사항 수정")
	void editInfo(
			@Schema(hidden = true) UserClaims claims,
			@Parameter(description = "공지사항 id") Long infoId,
			@RequestBody InfoEditRequest request
	);
	
	@Operation(summary = "[패짱] 공지사항 삭제")
	void deleteInfo(
			@Schema(hidden = true) UserClaims claims,
			@Parameter(description = "공지사항 id") Long infoId
	);
	
	@Operation(summary = "[관리자] 공지사항 수정")
	void editInfoByAdmin(
			@Parameter(description = "공지사항 id") Long infoId,
			@RequestBody InfoEditRequest request
	);
	
	@Operation(summary = "[관리자] 공지사항 삭제")
	void deleteInfoByAdmin(
			@Parameter(description = "공지사항 id") Long infoId
	);
}
