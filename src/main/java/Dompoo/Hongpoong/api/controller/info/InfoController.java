package Dompoo.Hongpoong.api.controller.info;

import Dompoo.Hongpoong.api.dto.info.request.InfoCreateRequest;
import Dompoo.Hongpoong.api.dto.info.request.InfoEditRequest;
import Dompoo.Hongpoong.api.dto.info.response.InfoDetailResponse;
import Dompoo.Hongpoong.api.dto.info.response.InfoResponse;
import Dompoo.Hongpoong.api.service.InfoService;
import Dompoo.Hongpoong.common.security.SecurePolicy;
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

    @Secured(SecurePolicy.ADMIN_ONLY)
    @PostMapping
    public void createInfo(@RequestBody InfoCreateRequest request) {
        service.createInfo(request, LocalDateTime.now());
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
