package Dompoo.Hongpoong.controller;

import Dompoo.Hongpoong.config.security.UserPrincipal;
import Dompoo.Hongpoong.request.member.MemberEditRequest;
import Dompoo.Hongpoong.request.member.MemberRoleEditRequest;
import Dompoo.Hongpoong.response.MemberResponse;
import Dompoo.Hongpoong.service.MemberService;
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
    public MemberResponse getStatus(@AuthenticationPrincipal UserPrincipal principal) {
        return service.getStatus(principal.getMemberId());
    }

    @PutMapping("")
    public void editMember(@AuthenticationPrincipal UserPrincipal principal, @RequestBody @Valid MemberEditRequest request) {
        service.editMember(principal.getMemberId(), request);
    }

    @DeleteMapping("")
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
