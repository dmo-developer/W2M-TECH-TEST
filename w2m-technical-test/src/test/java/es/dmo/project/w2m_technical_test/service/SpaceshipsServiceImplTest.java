package es.dmo.project.w2m_technical_test.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import es.dmo.project.w2m_technical_test.entity.SpaceshipEntity;
import es.dmo.project.w2m_technical_test.exception.SpaceshipNotFoundException;
import es.dmo.project.w2m_technical_test.mappers.SpaceshipsEntityVOMapperImpl;
import es.dmo.project.w2m_technical_test.model.SpaceshipRequestFilterDTO;
import es.dmo.project.w2m_technical_test.model.SpaceshipVO;
import es.dmo.project.w2m_technical_test.repository.SpaceshipsRepository;

/**
 * The test class for {@link SpaceshipsServiceImpl}
 * 
 * @author David de Miguel Otero
 */
@ExtendWith(MockitoExtension.class)
class SpaceshipsServiceImplTest {

	@Spy
	private SpaceshipsEntityVOMapperImpl mapper;

	@Mock
	private SpaceshipsRepository spaceshipsRepository;

	@InjectMocks
	private SpaceshipsServiceImpl service;

	@Captor
	private ArgumentCaptor<SpaceshipEntity> spEntityCaptor;

	@Captor
	private ArgumentCaptor<Long> spIdCaptor;

	@Test
	void testGetSpaceshipById_whenFindSpaceshipByID_thenReturnSearchedSpaceship() {
		final Long id = 1L;
		final String name = "new spaceShip";
		final SpaceshipEntity response = new SpaceshipEntity();
		response.setId(id);
		response.setName(name);
		when(spaceshipsRepository.findById(id)).thenReturn(response);
		final SpaceshipVO vo = service.getSpaceshipById(id);
		assertAll(() -> assertEquals(id, vo.getIdentifier()), () -> assertEquals(name, vo.getSpaceShipName()));
	}

	@Test
	void testGetSpaceshipById_whenNoFindSpaceshipByID_thenThrowsException() {
		final Long id = 1L;
		when(spaceshipsRepository.findById(id)).thenReturn(null);
		final SpaceshipNotFoundException exception = assertThrows(SpaceshipNotFoundException.class,
				() -> service.getSpaceshipById(id));
		assertEquals(String.valueOf(id), exception.getMessage());
	}

	@Test
	void testGetAllSpaceships_whenFindSpaceships_thenReturnSearchedSpaceship() {
		final Long id1 = 11L;
		final String name1 = "spaceship1's name";
		final SpaceshipRequestFilterDTO filter = SpaceshipRequestFilterDTO.builder().name(name1).page(0).size(2)
				.build();
		final SpaceshipEntity spaceship1 = new SpaceshipEntity();
		final List<SpaceshipEntity> spaceships = List.of(spaceship1);
		spaceship1.setId(id1);
		spaceship1.setName(name1);
		when(spaceshipsRepository.findByNameContaining(name1, PageRequest.of(0, 2))).thenReturn(spaceships);
		final List<SpaceshipVO> result = service.getAllSpaceships(filter);
		assertAll(() -> assertEquals(spaceships.size(), result.size()),
				() -> assertEquals(spaceships.get(0).getId(), result.get(0).getIdentifier()),
				() -> assertEquals(spaceships.get(0).getName(), result.get(0).getSpaceShipName()));
	}

	@Test
	void testGetAllSpaceships_whenNoFindSpaceships_thenReturnEmptyList() {
		final String name1 = "spaceship1's name";
		final SpaceshipRequestFilterDTO filter = SpaceshipRequestFilterDTO.builder().name(name1).page(0).size(2)
				.build();
		final List<SpaceshipEntity> spaceships = List.of();
		when(spaceshipsRepository.findByNameContaining(name1, PageRequest.of(0, 2))).thenReturn(spaceships);
		final List<SpaceshipVO> result = service.getAllSpaceships(filter);
		assertTrue(result.isEmpty());
	}

	@Test
	void testCreateNewSpaceship_whenSaveNewSpaceship_thenReturnCreatedEntityWithId() {
		final Long id = 99L;
		final String name = "already created spaceShip";
		final SpaceshipEntity request = new SpaceshipEntity();
		final SpaceshipEntity response = new SpaceshipEntity();
		request.setName(name);
		response.setId(id);
		response.setName(name);
		when(spaceshipsRepository.save(any(SpaceshipEntity.class))).thenReturn(response);
		final SpaceshipVO vo = service.createNewSpaceship(SpaceshipVO.builder().spaceShipName(name).build());
		assertAll(() -> verify(spaceshipsRepository, times(1)).save(spEntityCaptor.capture()),
				() -> assertEquals(name, spEntityCaptor.getValue().getName()),
				() -> assertEquals(id, vo.getIdentifier()), () -> assertEquals(name, vo.getSpaceShipName()));
	}

	@Test
	void testModifySpaceShipData_whenExistsEntityToModify_thenReturnModifiedEntity() {
		final Long rightId = 990L;
		final Long fakeId = 1L;
		final String name = "new spaceShip's name";
		final String anotherName = "the new name";
		final SpaceshipEntity found = new SpaceshipEntity();
		found.setId(rightId);
		found.setName(name);
		final SpaceshipEntity response = new SpaceshipEntity();
		response.setId(rightId);
		response.setName(anotherName);
		when(spaceshipsRepository.findById(rightId)).thenReturn(found);
		when(spaceshipsRepository.save(any(SpaceshipEntity.class))).thenReturn(response);
		final SpaceshipVO vo = service.modifySpaceShipData(rightId,
				SpaceshipVO.builder().identifier(fakeId).spaceShipName(name).build());
		assertAll(() -> verify(spaceshipsRepository, times(1)).save(spEntityCaptor.capture()),
				() -> assertEquals(found.getId(), spEntityCaptor.getValue().getId()),
				() -> assertEquals(found.getName(), spEntityCaptor.getValue().getName()),
				() -> assertEquals(rightId, vo.getIdentifier()),
				() -> assertEquals(anotherName, vo.getSpaceShipName()));
	}

	@Test
	void testModifySpaceShipData_whenNoExistsEntityToModify_thenThrowsException() {
		final Long id = 1L;
		final String name = "new spaceShip's name";
		when(spaceshipsRepository.findById(id)).thenReturn(null);
		final SpaceshipNotFoundException exception = assertThrows(SpaceshipNotFoundException.class,
				() -> service.modifySpaceShipData(id, SpaceshipVO.builder().spaceShipName(name).build()));
		assertEquals(String.valueOf(id), exception.getMessage());
	}

	@Test
	void testDeleteSpaceshipById_whenDeletesEntity_thenVoidValue() {
		final Long id = 55L;
		doNothing().when(spaceshipsRepository).deleteById(id);
		service.deleteSpaceshipById(id);
		assertAll(() -> verify(spaceshipsRepository, times(1)).deleteById(spIdCaptor.capture()),
				() -> assertEquals(id, spIdCaptor.getValue()));
	}

}
