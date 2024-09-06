package Dompoo.Hongpoong.domain.repository;

import Dompoo.Hongpoong.domain.entity.SignUp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SignUpRepository extends JpaRepository<SignUp, Long> {
    boolean existsByEmail(String email);
    Optional<SignUp> findByEmail(String email);
}
