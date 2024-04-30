package Dompoo.Hongpoong.controller;

import Dompoo.Hongpoong.config.security.UserPrincipal;
import Dompoo.Hongpoong.request.member.MemberEditRequest;
import Dompoo.Hongpoong.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService service;

    @PutMapping("")
    public void editMember(@AuthenticationPrincipal UserPrincipal principal, @RequestBody @Valid MemberEditRequest request) {
        service.editMember(principal.getMemberId(), request);
    }

    @DeleteMapping("")
    public void deleteMember(@AuthenticationPrincipal UserPrincipal principal) {
        service.deleteMember(principal.getMemberId());
    }
}
