package es.dmo.project.w2m_technical_test.service;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;

import es.dmo.project.w2m_technical_test.exception.SpaceshipNotFoundException;
import es.dmo.project.w2m_technical_test.mappers.SpaceshipsEntityVOMapper;
import es.dmo.project.w2m_technical_test.model.SpaceshipRequestFilterDTO;
import es.dmo.project.w2m_technical_test.model.SpaceshipVO;
import es.dmo.project.w2m_technical_test.repository.SpaceshipsRepository;
import lombok.AllArgsConstructor;

/**
 * The class contains the logic's implementation for {@link SpaceshipsService} interface
 * 
 * @author David de Miguel Otero
 */
@AllArgsConstructor
public class SpaceshipsServiceImpl implements SpaceshipsService {

	private SpaceshipsEntityVOMapper mapper;
	private SpaceshipsRepository spaceshipsRepository;

	@Override
	@Cacheable(value = "spaceship", key = "#id")
	public SpaceshipVO getSpaceshipById(final long id) {
		return Optional.ofNullable(mapper.toSpaceshipVO(spaceshipsRepository.findById(id)))
				.orElseThrow(() -> new SpaceshipNotFoundException(String.valueOf(id)));
	}

	@Override
	@Cacheable(value = "spaceship")
	public List<SpaceshipVO> getAllSpaceships(final SpaceshipRequestFilterDTO filter) {
		return mapper.spaceshipEntitiesToSpaceshipVOList(spaceshipsRepository.findByNameContaining(filter.getName(),
				PageRequest.of(filter.getPage(), filter.getSize())));
	}

	@Override
	public SpaceshipVO createNewSpaceship(final SpaceshipVO spaceship) {
		return mapper.toSpaceshipVO(spaceshipsRepository.save(mapper.toSpaceshipEntity(spaceship)));
	}

	@Override
	@CachePut(value = "spaceship", key = "#spaceship.identifier")
	public SpaceshipVO modifySpaceShipData(long id, final SpaceshipVO spaceship) {
		getSpaceshipById(id);
		spaceship.setIdentifier(id);
		return createNewSpaceship(spaceship);
	}

	@Override
	@CacheEvict(value = "spaceship", key = "#id")
	public void deleteSpaceshipById(final long id) {
		spaceshipsRepository.deleteById(id);
	}

}
