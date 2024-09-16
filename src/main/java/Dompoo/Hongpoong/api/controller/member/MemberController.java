package Dompoo.Hongpoong.api.controller.member;

import Dompoo.Hongpoong.api.dto.member.request.MemberEditRequest;
import Dompoo.Hongpoong.api.dto.member.request.MemberRoleEditRequest;
import Dompoo.Hongpoong.api.dto.member.response.MemberResponse;
import Dompoo.Hongpoong.api.dto.member.response.MemberStatusResponse;
import Dompoo.Hongpoong.api.service.MemberService;
import Dompoo.Hongpoong.common.security.SecurePolicy;
import Dompoo.Hongpoong.common.security.UserClaims;
import Dompoo.Hongpoong.common.security.annotation.LoginUser;
import Dompoo.Hongpoong.common.security.annotation.Secured;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController implements MemberApi {

    private final MemberService service;
    
    @Secured
    @GetMapping
    public List<MemberResponse> findAllMember() {
        return service.findAllMember();
    }
    
    @Secured
    @GetMapping("/status")
    public MemberStatusResponse findMyMemberDetail(@LoginUser UserClaims claims) {
        return service.findMyMemberDetail(claims.getId());
    }
    
    @Secured
    @PatchMapping
    public void editMyMember(@LoginUser UserClaims claims, @RequestBody @Valid MemberEditRequest request) {
        service.editMyMember(claims.getId(), request.toDto(), request.getPassword());
    }

    @Secured
    @DeleteMapping
    public void withDraw(@LoginUser UserClaims claims) {
        service.deleteMemberByAdmin(claims.getId());
    }
    
    @Secured(SecurePolicy.LEADER)
    @PatchMapping("/{memberId}")
    public void editMemberRole(@LoginUser UserClaims claims, @PathVariable Long memberId, @RequestBody @Valid MemberRoleEditRequest request) {
        service.editMemberAuth(claims.getId(), memberId, request.toDto());
    }
    
    @Secured(SecurePolicy.LEADER)
    @DeleteMapping("/{memberId}")
    public void deleteMember(@LoginUser UserClaims claims, @PathVariable Long memberId) {
        service.deleteMember(claims.getId(), memberId);
    }

    @Secured(SecurePolicy.ADMIN)
    @PatchMapping("/manage/{memberId}")
    public void editMemberRoleByAdmin(@PathVariable Long memberId, @RequestBody @Valid MemberRoleEditRequest request) {
        service.editMemberAuthByAdmin(memberId, request.toDto());
    }

    @Secured(SecurePolicy.ADMIN)
    @DeleteMapping("/manage/{memberId}")
    public void deleteMemberByAdmin(@PathVariable Long memberId) {
        service.deleteMemberByAdmin(memberId);
    }
}
