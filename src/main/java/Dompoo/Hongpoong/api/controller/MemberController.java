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
    @GetMapping("/status")
    public MemberStatusResponse getStatus(@LoginUser UserClaims claims) {
        return service.getStatus(claims.getId());
    }

    @Secured
    @PutMapping
    public void editMember(@LoginUser UserClaims claims, @RequestBody @Valid MemberEditRequest request) {
        service.editMember(claims.getId(), request.toDto());
    }

    @Secured
    @DeleteMapping
    public void deleteMember(@LoginUser UserClaims claims) {
        service.deleteMember(claims.getId());
    }

    @Secured(SecurePolicy.ADMIN_ONLY)
    @GetMapping("/manage")
    public List<MemberResponse> memberList() {
        return service.getList();
    }

    @Secured(SecurePolicy.ADMIN_ONLY)
    @PatchMapping("/manage/{id}")
    public void changeAuth(@PathVariable Long id, @RequestBody @Valid MemberRoleEditRequest request) {
        service.editRole(id, request);
    }

    @Secured(SecurePolicy.ADMIN_ONLY)
    @DeleteMapping("/manage/{id}")
    public void memberdelete(@PathVariable Long id) {
        service.deleteMember(id);
    }
}
