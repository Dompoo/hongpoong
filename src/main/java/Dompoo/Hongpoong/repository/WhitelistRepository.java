package Dompoo.Hongpoong.repository;

import Dompoo.Hongpoong.domain.Whitelist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WhitelistRepository extends JpaRepository<Whitelist, Long> {
    Boolean existsByEmail(String email);
    Optional<Whitelist> findByEmail(String email);
}
