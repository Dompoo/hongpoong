package Dompoo.Hongpoong.api.controller;

import Dompoo.Hongpoong.api.dto.request.info.InfoCreateRequest;
import Dompoo.Hongpoong.api.dto.request.info.InfoEditRequest;
import Dompoo.Hongpoong.api.dto.response.info.InfoDetailResponse;
import Dompoo.Hongpoong.api.dto.response.info.InfoResponse;
import Dompoo.Hongpoong.api.service.InfoService;
import Dompoo.Hongpoong.common.security.SecurePolicy;
import Dompoo.Hongpoong.common.security.Secured;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/info")
public class InfoController {

    private final InfoService service;

    @Secured(SecurePolicy.ADMIN_ONLY)
    @PostMapping
    public void createInfo(@RequestBody InfoCreateRequest request) {
        service.addInfo(request, LocalDateTime.now());
    }

    @Secured
    @GetMapping
    public List<InfoResponse> getInfoList() {
        return service.getList();
    }

    @Secured
    @GetMapping("/{infoId}")
    public InfoDetailResponse getInfoDetail(@PathVariable Long infoId) {
        return service.getDetail(infoId);
    }

    @Secured(SecurePolicy.ADMIN_ONLY)
    @PutMapping("/{infoId}")
    public void editInfo(@PathVariable Long infoId, @RequestBody InfoEditRequest request) {
        service.editInfo(infoId, request.toDto());
    }

    @Secured(SecurePolicy.ADMIN_ONLY)
    @DeleteMapping("/{infoId}")
    public void deleteInfo(@PathVariable Long infoId) {
        service.deleteInfo(infoId);
    }

}
