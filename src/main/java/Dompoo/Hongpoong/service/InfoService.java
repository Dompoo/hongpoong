package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.domain.Info;
import Dompoo.Hongpoong.exception.InfoNotFound;
import Dompoo.Hongpoong.repository.InfoRepository;
import Dompoo.Hongpoong.request.info.InfoCreateRequest;
import Dompoo.Hongpoong.request.info.InfoEditRequest;
import Dompoo.Hongpoong.response.InfoDetailResponse;
import Dompoo.Hongpoong.response.InfoListResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class InfoService {

    private final InfoRepository repository;

    public void addInfo(InfoCreateRequest request) {
        repository.save(Info.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .date(LocalDateTime.now())
                .build());
    }

    public List<InfoListResponse> getList() {
        return repository.findAll().stream()
                .map(InfoListResponse::new)
                .collect(Collectors.toList());
    }

    public InfoDetailResponse getDetail(Long infoId) {
        Info info = repository.findById(infoId)
                .orElseThrow(InfoNotFound::new);

        return new InfoDetailResponse(info);
    }

    public void editInfo(Long infoId, InfoEditRequest request) {
        Info info = repository.findById(infoId)
                .orElseThrow(InfoNotFound::new);

        if (request.getTitle() != null) info.setTitle(request.getTitle());
        if (request.getContent() != null) info.setContent(request.getContent());
    }

    public void deleteInfo(Long infoId) {
        Info info = repository.findById(infoId)
                .orElseThrow(InfoNotFound::new);

        repository.delete(info);
    }
}
