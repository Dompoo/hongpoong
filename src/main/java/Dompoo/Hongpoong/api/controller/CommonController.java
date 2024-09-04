package Dompoo.Hongpoong.api.controller;

import Dompoo.Hongpoong.api.dto.request.common.SettingSaveRequest;
import Dompoo.Hongpoong.api.dto.response.common.SettingResponse;
import Dompoo.Hongpoong.api.service.CommonService;
import Dompoo.Hongpoong.common.security.LoginUser;
import Dompoo.Hongpoong.common.security.Secured;
import Dompoo.Hongpoong.common.security.UserClaims;
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
