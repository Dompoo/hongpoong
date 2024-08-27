package Dompoo.Hongpoong.api.controller;

import Dompoo.Hongpoong.api.dto.request.common.SettingSaveRequest;
import Dompoo.Hongpoong.api.dto.response.common.SettingResponse;
import Dompoo.Hongpoong.common.security.UserPrincipal;
import Dompoo.Hongpoong.api.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/common")
public class CommonController {

    private final CommonService service;

    @GetMapping("/setting")
    public SettingResponse getSetting(@AuthenticationPrincipal UserPrincipal principal) {
        return service.getSetting(principal.getMemberId());
    }

    @PostMapping("/setting")
    public void saveSetting(@AuthenticationPrincipal UserPrincipal principal, @RequestBody SettingSaveRequest request) {
        service.saveSetting(principal.getMemberId(), request.toDto());
    }
}
