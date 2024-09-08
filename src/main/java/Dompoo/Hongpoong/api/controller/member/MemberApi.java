package Dompoo.Hongpoong.api.controller.member;

import Dompoo.Hongpoong.api.dto.member.MemberEditRequest;
import Dompoo.Hongpoong.api.dto.member.MemberRoleEditRequest;
import Dompoo.Hongpoong.api.dto.member.response.MemberResponse;
import Dompoo.Hongpoong.api.dto.member.response.MemberStatusResponse;
import Dompoo.Hongpoong.common.security.UserClaims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "2. 회원")
public interface MemberApi {
	
	@Operation(summary = "전체 회원 조회")
	List<MemberResponse> findAllMember();
	
	@Operation(summary = "내 정보 조회")
	MemberStatusResponse findMyMemberDetail(
			@Schema(hidden = true) UserClaims claims
	);
	
	@Operation(summary = "내 정보 수정")
	void editMyMember(
			@Schema(hidden = true) UserClaims claims,
			@RequestBody MemberEditRequest request
	);
	
	@Operation(summary = "회원 탈퇴")
	void withDraw(
			@Schema(hidden = true) UserClaims claims
	);
	
	@Operation(summary = "회원 권한 수정")
	void editMemberRole(
			@Parameter(description = "회원 id") Long memberId,
			@RequestBody MemberRoleEditRequest request
	);
	
	@Operation(summary = "회원 삭제")
	void deleteMember(
			@Parameter(description = "회원 id") Long memberId
	);
}
