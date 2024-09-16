package Dompoo.Hongpoong.domain.persistence.repository;

import Dompoo.Hongpoong.domain.domain.SignUp;

public interface SignUpRepository {
    
    boolean existsByEmail(String email);
    
    SignUp findByEmail(String email);
}
