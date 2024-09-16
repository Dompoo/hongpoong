package Dompoo.Hongpoong.domain.persistence.jpaRepository;

import Dompoo.Hongpoong.domain.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageJpaRepository extends JpaRepository<ChatMessage, Long> {
}
