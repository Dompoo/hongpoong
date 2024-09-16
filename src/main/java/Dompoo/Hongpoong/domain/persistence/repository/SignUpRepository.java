package Dompoo.Hongpoong.domain.persistence.repository;

import Dompoo.Hongpoong.domain.domain.SignUp;

import java.util.List;

public interface SignUpRepository {
    
    boolean existsByEmail(String email);
    
    SignUp findByEmail(String email);
    
    void save(SignUp signUp);
    
    SignUp findById(Long signupId);
    
    void delete(SignUp signUp);
    
    List<SignUp> findAll();
}
