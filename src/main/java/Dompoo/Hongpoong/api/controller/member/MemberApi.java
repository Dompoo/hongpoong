package Dompoo.Hongpoong.api.controller.member;

import Dompoo.Hongpoong.api.dto.member.MemberEditRequest;
import Dompoo.Hongpoong.api.dto.member.MemberResponse;
import Dompoo.Hongpoong.api.dto.member.MemberRoleEditRequest;
import Dompoo.Hongpoong.api.dto.member.MemberStatusResponse;
import Dompoo.Hongpoong.common.security.UserClaims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "2. 회원")
public interface MemberApi {
	
	@Operation(summary = "전체 회원 조회")
	List<MemberResponse> findAllMember();
	
	@Operation(summary = "내 정보 조회")
	MemberStatusResponse findMyMemberDetail(UserClaims claims);
	
	@Operation(summary = "내 정보 수정")
	void editMyMember(UserClaims claims, MemberEditRequest request);
	
	@Operation(summary = "회원 탈퇴")
	void withDraw(UserClaims claims);
	
	@Operation(summary = "회원 권한 수정")
	void editMemberRole(Long id, MemberRoleEditRequest request);
	
	@Operation(summary = "회원 삭제")
	void deleteMember(Long id);
}
