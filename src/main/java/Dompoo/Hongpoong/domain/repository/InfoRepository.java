package Dompoo.Hongpoong.domain.repository;

import Dompoo.Hongpoong.domain.entity.Info;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InfoRepository extends JpaRepository<Info, Long> {
	@Query("SELECT i from Info i JOIN FETCH i.member WHERE i.id = :infoId")
	Optional<Info> findByIdFetchJoinMember(@Param("infoId") Long infoId);
}
