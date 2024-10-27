package es.dmo.project.w2m_technical_test.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.dmo.project.w2m_technical_test.entity.SpaceshipEntity;

@Repository
public interface SpaceshipsRepository extends PagingAndSortingRepository<SpaceshipEntity, Long> {

	public SpaceshipEntity findById(long id);

	// In others databases we could do without query. H2 doesn't seem to support
	// Containing word
	@Query("FROM SpaceshipEntity sh WHERE sh.name LIKE  %:name%")
	public List<SpaceshipEntity> findByNameContaining(@Param("name") String name, Pageable pageable);

	public SpaceshipEntity save(SpaceshipEntity spaceship);

	public void deleteById(long id);

}
