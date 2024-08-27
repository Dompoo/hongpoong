package Dompoo.Hongpoong.api.service;

import Dompoo.Hongpoong.api.dto.request.info.InfoCreateRequest;
import Dompoo.Hongpoong.api.dto.request.info.InfoEditDto;
import Dompoo.Hongpoong.api.dto.response.info.InfoDetailResponse;
import Dompoo.Hongpoong.api.dto.response.info.InfoResponse;
import Dompoo.Hongpoong.common.exception.impl.InfoNotFound;
import Dompoo.Hongpoong.domain.entity.Info;
import Dompoo.Hongpoong.domain.repository.InfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InfoService {

    private final InfoRepository repository;

    @Transactional
    public void addInfo(InfoCreateRequest request, LocalDateTime now) {
        repository.save(request.toInfo(now));
    }

    @Transactional(readOnly = true)
    public List<InfoResponse> getList() {
        return repository.findAll().stream()
                .map(InfoResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public InfoDetailResponse getDetail(Long infoId) {
        Info info = repository.findById(infoId)
                .orElseThrow(InfoNotFound::new);

        return InfoDetailResponse.from(info);
    }

    @Transactional
    public void editInfo(Long infoId, InfoEditDto dto) {
        Info info = repository.findById(infoId)
                .orElseThrow(InfoNotFound::new);
        
        info.edit(dto);
    }

    @Transactional
    public void deleteInfo(Long infoId) {
        Info info = repository.findById(infoId)
                .orElseThrow(InfoNotFound::new);

        repository.delete(info);
    }
}
