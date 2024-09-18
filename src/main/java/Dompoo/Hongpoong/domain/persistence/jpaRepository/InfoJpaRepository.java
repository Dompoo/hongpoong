package Dompoo.Hongpoong.domain.persistence.jpaRepository;

import Dompoo.Hongpoong.domain.jpaEntity.InfoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InfoJpaRepository extends JpaRepository<InfoJpaEntity, Long> {
	
	@Query("SELECT i from InfoJpaEntity i JOIN FETCH i.memberJpaEntity WHERE i.id = :infoId")
	Optional<InfoJpaEntity> findByIdFetchJoinMember(
			@Param("infoId") Long infoId
	);
}
