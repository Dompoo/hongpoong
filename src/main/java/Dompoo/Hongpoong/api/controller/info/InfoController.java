package Dompoo.Hongpoong.api.controller.info;

import Dompoo.Hongpoong.api.dto.info.request.InfoCreateRequest;
import Dompoo.Hongpoong.api.dto.info.request.InfoEditRequest;
import Dompoo.Hongpoong.api.dto.info.response.InfoDetailResponse;
import Dompoo.Hongpoong.api.dto.info.response.InfoResponse;
import Dompoo.Hongpoong.api.service.InfoService;
import Dompoo.Hongpoong.common.security.SecurePolicy;
import Dompoo.Hongpoong.common.security.UserClaims;
import Dompoo.Hongpoong.common.security.annotation.LoginUser;
import Dompoo.Hongpoong.common.security.annotation.Secured;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/info")
public class InfoController implements InfoApi {

    private final InfoService service;

    @Secured(SecurePolicy.ADMIN_LEADER)
    @PostMapping
    public void createInfo(@LoginUser UserClaims claims, @RequestBody InfoCreateRequest request) {
        service.createInfo(claims.getId(), request, LocalDateTime.now());
    }

    @Secured
    @GetMapping
    public List<InfoResponse> findAllInfo() {
        return service.findAllInfo();
    }

    @Secured
    @GetMapping("/{infoId}")
    public InfoDetailResponse findInfoDetail(@PathVariable Long infoId) {
        return service.findInfoDetail(infoId);
    }

    @Secured(SecurePolicy.LEADER_ONLY)
    @PutMapping("/{infoId}")
    public void editInfo(@LoginUser UserClaims claims, @PathVariable Long infoId, @RequestBody InfoEditRequest request) {
        service.editInfo(claims.getId(), infoId, request.toDto());
    }

    @Secured(SecurePolicy.LEADER_ONLY)
    @DeleteMapping("/{infoId}")
    public void deleteInfo(@LoginUser UserClaims claims, @PathVariable Long infoId) {
        service.deleteInfo(claims.getId(), infoId);
    }
    
    @Secured(SecurePolicy.ADMIN_ONLY)
    @PutMapping("/manage/{infoId}")
    public void editInfoByAdmin(@PathVariable Long infoId, @RequestBody InfoEditRequest request) {
        service.editInfoByAdmin(infoId, request.toDto());
    }
    
    @Secured(SecurePolicy.ADMIN_ONLY)
    @DeleteMapping("/manage/{infoId}")
    public void deleteInfoByAdmin(@PathVariable Long infoId) {
        service.deleteInfoByAdmin(infoId);
    }

}
