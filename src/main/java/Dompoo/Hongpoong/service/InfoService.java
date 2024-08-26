package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.api.dto.request.info.InfoCreateRequest;
import Dompoo.Hongpoong.api.dto.request.info.InfoEditDto;
import Dompoo.Hongpoong.api.dto.response.info.InfoDetailResponse;
import Dompoo.Hongpoong.api.dto.response.info.InfoResponse;
import Dompoo.Hongpoong.common.exception.impl.InfoNotFound;
import Dompoo.Hongpoong.domain.Info;
import Dompoo.Hongpoong.repository.InfoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class InfoService {

    private final InfoRepository repository;

    public void addInfo(InfoCreateRequest request, LocalDateTime now) {
        repository.save(request.toInfo(now));
    }

    public List<InfoResponse> getList() {
        return repository.findAll().stream()
                .map(InfoResponse::from)
                .toList();
    }

    public InfoDetailResponse getDetail(Long infoId) {
        Info info = repository.findById(infoId)
                .orElseThrow(InfoNotFound::new);

        return InfoDetailResponse.from(info);
    }

    public void editInfo(Long infoId, InfoEditDto dto) {
        Info info = repository.findById(infoId)
                .orElseThrow(InfoNotFound::new);
        
        info.edit(dto);
    }

    public void deleteInfo(Long infoId) {
        Info info = repository.findById(infoId)
                .orElseThrow(InfoNotFound::new);

        repository.delete(info);
    }
}
