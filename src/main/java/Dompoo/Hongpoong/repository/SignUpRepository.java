package Dompoo.Hongpoong.repository;

import Dompoo.Hongpoong.domain.SignUp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SignUpRepository extends JpaRepository<SignUp, Long> {
    Boolean existsByEmail(String email);
    Optional<SignUp> findByEmail(String email);
}
