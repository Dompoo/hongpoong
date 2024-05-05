package Dompoo.Hongpoong.controller;

import Dompoo.Hongpoong.config.security.UserPrincipal;
import Dompoo.Hongpoong.request.common.SettingSaveRequest;
import Dompoo.Hongpoong.response.SettingResponse;
import Dompoo.Hongpoong.service.CommonService;
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
        service.saveSetting(principal.getMemberId(), request);
    }
}
