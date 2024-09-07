package Dompoo.Hongpoong.api.controller;

import Dompoo.Hongpoong.api.dto.request.member.MemberEditRequest;
import Dompoo.Hongpoong.api.dto.request.member.MemberRoleEditRequest;
import Dompoo.Hongpoong.api.dto.response.member.MemberResponse;
import Dompoo.Hongpoong.api.dto.response.member.MemberStatusResponse;
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
public class MemberController {

    private final MemberService service;
    
    @Secured
    @GetMapping
    public List<MemberResponse> getAllMember() {
        return service.getAllMember();
    }
    
    @Secured
    @GetMapping("/status")
    public MemberStatusResponse getMyDetail(@LoginUser UserClaims claims) {
        return service.getMyDetail(claims.getId());
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
