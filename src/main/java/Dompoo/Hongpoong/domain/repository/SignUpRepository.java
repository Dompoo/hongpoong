package Dompoo.Hongpoong.domain.repository;

import Dompoo.Hongpoong.domain.entity.SignUp;

import java.util.Optional;

public interface SignUpRepository {
    
    boolean existsByEmail(String email);
    
    Optional<SignUp> findByEmail(String email);
}
