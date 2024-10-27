package es.dmo.project.w2m_technical_test.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import es.dmo.project.api.DmoApi;
import es.dmo.project.model.SpaceshipContract;
import es.dmo.project.w2m_technical_test.mappers.SpaceshipsVOContractMapper;
import es.dmo.project.w2m_technical_test.model.SpaceshipRequestFilterDTO;
import es.dmo.project.w2m_technical_test.service.SpaceshipsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The class contains the implementation for {@link DmoApi} interface
 * 
 * @author David de Miguel Otero
 */
@Slf4j
@RestController
//@RequestMapping(SpaceshipsConstants.SPACESHIP_PATH)
@AllArgsConstructor
public class SpaceshipsController implements DmoApi {

	private SpaceshipsVOContractMapper mapper;
	private SpaceshipsService spaceshipsService;

	@Override
	public ResponseEntity<SpaceshipContract> _getSpaceshipById(final Long id) {
		return ResponseEntity.ok(mapper.toSpaceshipContract(spaceshipsService.getSpaceshipById(id)));
	}

//	@GetMapping(SpaceshipsConstants.BY_ID_PATH)
//	public ResponseEntity<SpaceshipVO> getSpaceshipById(@PathVariable final long id) {
//		return ResponseEntity.ok(spaceshipsService.getSpaceshipById(id));
//	}

	@Override
	public ResponseEntity<List<SpaceshipContract>> _getAllSpaceships(@Valid final String name,
			@Valid final Integer page, @Valid final Integer size) {
		return ResponseEntity.ok(mapper.spaceshipVOToSpaceshipContractList(spaceshipsService.getAllSpaceships(
				SpaceshipRequestFilterDTO.builder().name(name).page(page.intValue()).size(size.intValue()).build())));
	}

//	@GetMapping
//	public ResponseEntity<List<SpaceshipVO>> getAllSpaceships(
//			@RequestParam(required = false, defaultValue = "") final String name,
//			@RequestParam(required = false, defaultValue = "0") final int page,
//			@RequestParam(required = false, defaultValue = "99") final int size) {
//		return ResponseEntity.ok(spaceshipsService
//				.getAllSpaceships(SpaceshipRequestFilterDTO.builder().name(name).page(page).size(size).build()));
//	}

	@Override
	public ResponseEntity<SpaceshipContract> _createNewSpaceship(@Valid final SpaceshipContract spaceshipContract) {
		return ResponseEntity.status(HttpStatus.CREATED).body(mapper
				.toSpaceshipContract(spaceshipsService.createNewSpaceship(mapper.toSpaceshipVO(spaceshipContract))));
	}

//	@PostMapping
//	public ResponseEntity<SpaceshipVO> createNewSpaceship(@RequestBody final SpaceshipVO spaceShip) {
//		return ResponseEntity.status(HttpStatus.CREATED).body(spaceshipsService.createNewSpaceship(spaceShip));
//	}

	@Override
	public ResponseEntity<SpaceshipContract> _modifySpaceshipData(final Long id,
			@Valid final SpaceshipContract spaceship) {
		return ResponseEntity.status(HttpStatus.OK).body(
				mapper.toSpaceshipContract(spaceshipsService.modifySpaceShipData(id, mapper.toSpaceshipVO(spaceship))));
	}

//	@PutMapping(SpaceshipsConstants.BY_ID_PATH)
//	public ResponseEntity<SpaceshipVO> modifySpaceshipData(@PathVariable final long id,
//			@RequestBody final SpaceshipVO spaceship) {
//		return ResponseEntity.status(HttpStatus.OK).body(spaceshipsService.modifySpaceShipData(id, spaceship));
//	}

	@Override
	public ResponseEntity<Void> _deleteSpaceship(final Long id) {
		spaceshipsService.deleteSpaceshipById(id);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

//	@DeleteMapping(SpaceshipsConstants.BY_ID_PATH)
//	public ResponseEntity<Void> deleteSpaceship(@PathVariable final long id) {
//		spaceshipsService.deleteSpaceshipById(id);
//		return ResponseEntity.status(HttpStatus.OK).build();
//	}

}
