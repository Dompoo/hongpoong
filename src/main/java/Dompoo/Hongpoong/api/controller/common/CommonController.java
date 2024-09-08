package Dompoo.Hongpoong.api.controller.common;

import Dompoo.Hongpoong.api.dto.common.SettingEditRequest;
import Dompoo.Hongpoong.api.dto.common.SettingResponse;
import Dompoo.Hongpoong.api.service.CommonService;
import Dompoo.Hongpoong.common.security.UserClaims;
import Dompoo.Hongpoong.common.security.annotation.LoginUser;
import Dompoo.Hongpoong.common.security.annotation.Secured;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/common")
public class CommonController implements CommonApi {

    private final CommonService service;

    @Secured
    @GetMapping("/setting")
    public SettingResponse findMySetting(@LoginUser UserClaims claims) {
        return service.findMySetting(claims.getId());
    }

    @Secured
    @PostMapping("/setting")
    public void editSetting(@LoginUser UserClaims claims, @RequestBody SettingEditRequest request) {
        service.editSetting(claims.getId(), request.toDto());
    }
}
