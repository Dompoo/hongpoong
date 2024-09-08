package Dompoo.Hongpoong.api.controller;

import Dompoo.Hongpoong.api.dto.common.SettingResponse;
import Dompoo.Hongpoong.api.dto.common.SettingSaveRequest;
import Dompoo.Hongpoong.api.service.CommonService;
import Dompoo.Hongpoong.common.security.UserClaims;
import Dompoo.Hongpoong.common.security.annotation.LoginUser;
import Dompoo.Hongpoong.common.security.annotation.Secured;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/common")
public class CommonController {

    private final CommonService service;

    @Secured
    @GetMapping("/setting")
    public SettingResponse getSetting(@LoginUser UserClaims claims) {
        return service.getSetting(claims.getId());
    }

    @Secured
    @PostMapping("/setting")
    public void saveSetting(@LoginUser UserClaims claims, @RequestBody SettingSaveRequest request) {
        service.saveSetting(claims.getId(), request.toDto());
    }
}
