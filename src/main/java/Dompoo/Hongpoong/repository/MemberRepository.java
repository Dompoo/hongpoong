package Dompoo.Hongpoong.repository;

import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.domain.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    boolean existsByRole(Role role);
}
