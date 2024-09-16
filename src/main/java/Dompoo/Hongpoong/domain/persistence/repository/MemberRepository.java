package Dompoo.Hongpoong.domain.persistence.repository;

import Dompoo.Hongpoong.domain.domain.Member;
import Dompoo.Hongpoong.domain.enums.Role;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    
    List<Member> findAllByIdIn(List<Long> memberIds);
    
    boolean existsByEmail(String email);
    
    Optional<Member> findByEmail(String email);
    
    Member findByIdAndEmail(Long id, String email);
    
    boolean existsByRole(Role role);
    
    Optional<Member> findById(Long memberId);
    
    void save(Member member);
}
