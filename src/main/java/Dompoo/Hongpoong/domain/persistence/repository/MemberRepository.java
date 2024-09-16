package Dompoo.Hongpoong.domain.persistence.repository;

import Dompoo.Hongpoong.domain.domain.Member;
import Dompoo.Hongpoong.domain.enums.Role;

import java.util.List;

public interface MemberRepository {
    
    List<Member> findAllByIdIn(List<Long> memberIds);
    
    boolean existsByEmail(String email);
    
    Member findByEmail(String email);
    
    Member findByIdAndEmail(Long id, String email);
    
    boolean existsByRole(Role role);
}
