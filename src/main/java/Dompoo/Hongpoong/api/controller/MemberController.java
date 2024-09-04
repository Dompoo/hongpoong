package Dompoo.Hongpoong.api.controller;

import Dompoo.Hongpoong.api.dto.request.member.MemberEditRequest;
import Dompoo.Hongpoong.api.dto.request.member.MemberRoleEditRequest;
import Dompoo.Hongpoong.api.dto.response.member.MemberResponse;
import Dompoo.Hongpoong.api.dto.response.member.MemberStatusResponse;
import Dompoo.Hongpoong.api.service.MemberService;
import Dompoo.Hongpoong.common.security.LoginUser;
import Dompoo.Hongpoong.common.security.UserClaims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService service;

    @GetMapping("/status")
    public MemberStatusResponse getStatus(@LoginUser UserClaims claims) {
        return service.getStatus(claims.getId());
    }

    @PutMapping
    public void editMember(@LoginUser UserClaims claims, @RequestBody @Valid MemberEditRequest request) {
        service.editMember(claims.getId(), request.toDto());
    }

    @DeleteMapping
    public void deleteMember(@LoginUser UserClaims claims) {
        service.deleteMember(claims.getId());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/manage")
    public List<MemberResponse> memberList() {
        return service.getList();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/manage/{id}")
    public void changeAuth(@PathVariable Long id, @RequestBody @Valid MemberRoleEditRequest request) {
        service.editRole(id, request);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/manage/{id}")
    public void memberdelete(@PathVariable Long id) {
        service.deleteMember(id);
    }
}
