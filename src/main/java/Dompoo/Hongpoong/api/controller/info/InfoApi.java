package Dompoo.Hongpoong.api.controller.info;

import Dompoo.Hongpoong.api.dto.info.InfoCreateRequest;
import Dompoo.Hongpoong.api.dto.info.InfoDetailResponse;
import Dompoo.Hongpoong.api.dto.info.InfoEditRequest;
import Dompoo.Hongpoong.api.dto.info.InfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "6. 공지사항")
public interface InfoApi {
	
	@Operation(summary = "공지사항 작성")
	void createInfo(InfoCreateRequest request);
	
	@Operation(summary = "공지사항 전체 조회")
	List<InfoResponse> findAllInfo();
	
	@Operation(summary = "공지사항 상세 조회")
	InfoDetailResponse findInfoDetail(Long infoId);
	
	@Operation(summary = "공지사항 수정")
	void editInfo(Long infoId, InfoEditRequest request);
	
	@Operation(summary = "공지사항 삭제")
	void deleteInfo(Long infoId);
}
