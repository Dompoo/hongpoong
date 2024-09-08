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
    @PutMapping
    public void editMyMember(@LoginUser UserClaims claims, @RequestBody @Valid MemberEditRequest request) {
        service.editMyMember(claims.getId(), request.toDto());
    }

    @Secured
    @DeleteMapping
    public void withDraw(@LoginUser UserClaims claims) {
        service.deleteMember(claims.getId());
    }

    @Secured(SecurePolicy.ADMIN_ONLY)
    @PatchMapping("/manage/{memberId}")
    public void editMemberRole(@PathVariable Long memberId, @RequestBody @Valid MemberRoleEditRequest request) {
        service.editMemberAuth(memberId, request);
    }

    @Secured(SecurePolicy.ADMIN_ONLY)
    @DeleteMapping("/manage/{memberId}")
    public void deleteMember(@PathVariable Long memberId) {
        service.deleteMember(memberId);
    }
}
