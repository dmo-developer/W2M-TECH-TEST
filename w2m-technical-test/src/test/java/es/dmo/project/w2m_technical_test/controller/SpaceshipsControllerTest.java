package es.dmo.project.w2m_technical_test.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.dmo.project.model.SpaceshipContract;
import es.dmo.project.w2m_technical_test.configuration.constants.SpaceshipsConstants;
import es.dmo.project.w2m_technical_test.mappers.SpaceshipsVOContractMapperImpl;
import es.dmo.project.w2m_technical_test.model.SpaceshipRequestFilterDTO;
import es.dmo.project.w2m_technical_test.model.SpaceshipVO;
import es.dmo.project.w2m_technical_test.service.SpaceshipsService;

/**
 * The test class for {@link SpaceshipsController}
 * 
 * @author David de Miguel Otero
 */
@ExtendWith(MockitoExtension.class)
class SpaceshipsControllerTest {

	@Spy
	private SpaceshipsVOContractMapperImpl mapper;

	@Mock
	private SpaceshipsService spaceshipsService;

	private SpaceshipsController spaceshipsController;

	private MockMvc mockmvc;

	@Captor
	private ArgumentCaptor<SpaceshipRequestFilterDTO> filterCaptor;

	@Captor
	private ArgumentCaptor<SpaceshipVO> spaceshipCaptor;

	@Captor
	private ArgumentCaptor<Long> longCaptor;

	@Spy
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		spaceshipsController = new SpaceshipsController(mapper, spaceshipsService);
		mockmvc = MockMvcBuilders.standaloneSetup(spaceshipsController).build();
	}

	@Test
	void testGetSpaceshipById_whenGetsAnExistingSpaceship_thenReturnSpaceship() throws Exception {
		final String spaceshipId = "111";
		final String spaceshipName = "The greatest name for a spaceship";

		when(spaceshipsService.getSpaceshipById(Long.valueOf(spaceshipId))).thenReturn(
				SpaceshipVO.builder().identifier(Long.valueOf(spaceshipId)).spaceShipName(spaceshipName).build());

		mockmvc.perform(get(SpaceshipsConstants.SPACESHIP_PATH.concat(SpaceshipsConstants.BY_ID_PATH), spaceshipId))
				.andExpect(status().isOk()).andExpect(jsonPath("$.identifier").value(spaceshipId))
				.andExpect(jsonPath("$.spaceShipName").value(spaceshipName));
	}

	@Test
	void testGetAllSpaceships_whenCallWithoutFilterValues_thenReturnAllSpaceships() throws Exception {
		final String spaceshipId = "333";
		final String spaceshipName = "The greatest name for a spaceship";
		final String anotherSpaceshipId = "777";
		final String anotherSpaceshipName = "The second greatest name for a spaceship";

		when(spaceshipsService.getAllSpaceships(any(SpaceshipRequestFilterDTO.class))).thenReturn(List.of(
				SpaceshipVO.builder().identifier(Long.valueOf(spaceshipId)).spaceShipName(spaceshipName).build(),
				SpaceshipVO.builder().identifier(Long.valueOf(anotherSpaceshipId)).spaceShipName(anotherSpaceshipName)
						.build()));

		mockmvc.perform(get(SpaceshipsConstants.SPACESHIP_PATH)).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].identifier").value(spaceshipId))
				.andExpect(jsonPath("$[0].spaceShipName").value(spaceshipName))
				.andExpect(jsonPath("$[1].identifier").value(anotherSpaceshipId))
				.andExpect(jsonPath("$[1].spaceShipName").value(anotherSpaceshipName));

		assertAll(() -> verify(spaceshipsService, times(1)).getAllSpaceships(filterCaptor.capture()),
				() -> assertEquals("", filterCaptor.getValue().getName()),
				() -> assertEquals(0, filterCaptor.getValue().getPage()),
				() -> assertEquals(99, filterCaptor.getValue().getSize()));
	}

	@Test
	void testCreateNewSpaceship_whenSendANewEntityToSave_thenReturnSameEntityWithAssignedId() throws Exception {
		final Long spaceshipId = 123L;
		final String spaceshipName = "The name for a new spaceship";
		final SpaceshipContract spaceshipContract = new SpaceshipContract();
		spaceshipContract.setIdentifier(spaceshipId);
		spaceshipContract.setSpaceShipName(spaceshipName);
		final String spaceshipContractStr = objectMapper.writeValueAsString(spaceshipContract);

		when(spaceshipsService.createNewSpaceship(any(SpaceshipVO.class))).thenReturn(
				SpaceshipVO.builder().identifier(Long.valueOf(spaceshipId)).spaceShipName(spaceshipName).build());

		mockmvc.perform(post(SpaceshipsConstants.SPACESHIP_PATH).contentType(MediaType.APPLICATION_JSON)
				.content(spaceshipContractStr)).andExpect(status().isCreated())
				.andExpect(jsonPath("$.identifier").value(spaceshipId))
				.andExpect(jsonPath("$.spaceShipName").value(spaceshipName));

		assertAll(() -> verify(spaceshipsService, times(1)).createNewSpaceship(spaceshipCaptor.capture()),
				() -> assertEquals(spaceshipId, spaceshipCaptor.getValue().getIdentifier()),
				() -> assertEquals(spaceshipName, spaceshipCaptor.getValue().getSpaceShipName()));
	}

	@Test
	void testModifySpaceShipData_whenModifyAnExistingEntity_thenReturnModifiedEntity() throws Exception {
		final Long spaceshipId = 123L;
		final String spaceshipName = "The name for a new spaceship";
		final SpaceshipContract spaceshipContract = new SpaceshipContract();
		spaceshipContract.setSpaceShipName(spaceshipName);
		final String spaceshipContractStr = objectMapper.writeValueAsString(spaceshipContract);

		when(spaceshipsService.modifySpaceShipData(anyLong(), any(SpaceshipVO.class))).thenReturn(
				SpaceshipVO.builder().identifier(Long.valueOf(spaceshipId)).spaceShipName(spaceshipName).build());

		mockmvc.perform(put(SpaceshipsConstants.SPACESHIP_PATH.concat(SpaceshipsConstants.BY_ID_PATH), spaceshipId)
				.contentType(MediaType.APPLICATION_JSON).content(spaceshipContractStr)).andExpect(status().isOk())
				.andExpect(jsonPath("$.identifier").value(spaceshipId))
				.andExpect(jsonPath("$.spaceShipName").value(spaceshipName));

		assertAll(
				() -> verify(spaceshipsService, times(1)).modifySpaceShipData(longCaptor.capture(),
						spaceshipCaptor.capture()),
				() -> assertEquals(spaceshipId, longCaptor.getValue()),
				() -> assertNull(spaceshipCaptor.getValue().getIdentifier()),
				() -> assertEquals(spaceshipName, spaceshipCaptor.getValue().getSpaceShipName()));
	}

	@Test
	void testDeleteSpaceshipById_whenDeletesExistingEntity_thenReturnNothing() throws Exception {
		final String spaceshipId = "111";

		doNothing().when(spaceshipsService).deleteSpaceshipById(Long.valueOf(spaceshipId));

		mockmvc.perform(delete(SpaceshipsConstants.SPACESHIP_PATH.concat(SpaceshipsConstants.BY_ID_PATH), spaceshipId))
				.andExpect(status().isOk());
	}

}
