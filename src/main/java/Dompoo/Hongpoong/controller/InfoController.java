package Dompoo.Hongpoong.controller;

import Dompoo.Hongpoong.request.info.InfoCreateRequest;
import Dompoo.Hongpoong.request.info.InfoEditRequest;
import Dompoo.Hongpoong.response.InfoDetailResponse;
import Dompoo.Hongpoong.response.InfoListResponse;
import Dompoo.Hongpoong.service.InfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/info")
public class InfoController {

    private final InfoService service;

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

    //TODO: 관리자 API
    @PutMapping("/{infoId}")
    public void editInfo(@PathVariable Long infoId, @RequestBody InfoEditRequest request) {
        service.editInfo(infoId, request);
    }

    //TODO: 관리자 API
    @DeleteMapping("/{infoId}")
    public void deleteInfo(@PathVariable Long infoId) {
        service.deleteInfo(infoId);
    }

}
