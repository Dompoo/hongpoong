package Dompoo.Hongpoong.domain.repository;

import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.enums.Club;
import Dompoo.Hongpoong.domain.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findAllByIdIn(List<Long> memberIds);
    boolean existsByEmail(String email);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByClubAndRole(Club club, Role role);
}
