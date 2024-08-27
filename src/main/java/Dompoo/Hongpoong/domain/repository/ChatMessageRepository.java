package Dompoo.Hongpoong.domain.repository;

import Dompoo.Hongpoong.domain.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}
