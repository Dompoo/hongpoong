package Dompoo.Hongpoong.api.controller;

import Dompoo.Hongpoong.api.dto.request.member.MemberEditRequest;
import Dompoo.Hongpoong.api.dto.request.member.MemberRoleEditRequest;
import Dompoo.Hongpoong.api.dto.response.member.MemberResponse;
import Dompoo.Hongpoong.api.dto.response.member.MemberStatusResponse;
import Dompoo.Hongpoong.api.service.MemberService;
import Dompoo.Hongpoong.common.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService service;

    @GetMapping("/status")
    public MemberStatusResponse getStatus(@AuthenticationPrincipal UserPrincipal principal) {
        return service.getStatus(principal.getMemberId());
    }

    @PutMapping
    public void editMember(@AuthenticationPrincipal UserPrincipal principal, @RequestBody @Valid MemberEditRequest request) {
        service.editMember(principal.getMemberId(), request.toDto());
    }

    @DeleteMapping
    public void deleteMember(@AuthenticationPrincipal UserPrincipal principal) {
        service.deleteMember(principal.getMemberId());
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
