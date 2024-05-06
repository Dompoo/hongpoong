package Dompoo.Hongpoong.controller;

import Dompoo.Hongpoong.request.info.InfoCreateRequest;
import Dompoo.Hongpoong.request.info.InfoEditRequest;
import Dompoo.Hongpoong.response.info.InfoDetailResponse;
import Dompoo.Hongpoong.response.info.InfoListResponse;
import Dompoo.Hongpoong.service.InfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/info")
public class InfoController {

    private final InfoService service;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("")
    public void createInfo(@RequestBody InfoCreateRequest request) {
        service.addInfo(request);
    }

    @GetMapping("")
    public List<InfoListResponse> getInfoList() {
        return service.getList();
    }

    @GetMapping("/{infoId}")
    public InfoDetailResponse getInfoDetail(@PathVariable Long infoId) {
        return service.getDetail(infoId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{infoId}")
    public void editInfo(@PathVariable Long infoId, @RequestBody InfoEditRequest request) {
        service.editInfo(infoId, request);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{infoId}")
    public void deleteInfo(@PathVariable Long infoId) {
        service.deleteInfo(infoId);
    }

}
