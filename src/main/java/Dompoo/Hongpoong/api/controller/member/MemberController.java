package Dompoo.Hongpoong.api.controller.member;

import Dompoo.Hongpoong.api.dto.member.MemberEditRequest;
import Dompoo.Hongpoong.api.dto.member.MemberResponse;
import Dompoo.Hongpoong.api.dto.member.MemberRoleEditRequest;
import Dompoo.Hongpoong.api.dto.member.MemberStatusResponse;
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
    @PatchMapping("/manage/{id}")
    public void editMemberRole(@PathVariable Long id, @RequestBody @Valid MemberRoleEditRequest request) {
        service.editMemberAuth(id, request);
    }

    @Secured(SecurePolicy.ADMIN_ONLY)
    @DeleteMapping("/manage/{id}")
    public void deleteMember(@PathVariable Long id) {
        service.deleteMember(id);
    }
}
