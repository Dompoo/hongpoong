package Dompoo.Hongpoong.repository;

import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.domain.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findAllByIdIn(List<Long> memberIds);
    Optional<Member> findByEmail(String email);
    boolean existsByRole(Role role);
}
