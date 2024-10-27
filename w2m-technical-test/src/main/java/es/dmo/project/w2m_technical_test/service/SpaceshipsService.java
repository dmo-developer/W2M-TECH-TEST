package es.dmo.project.w2m_technical_test.service;

import java.util.List;

import es.dmo.project.w2m_technical_test.model.SpaceshipRequestFilterDTO;
import es.dmo.project.w2m_technical_test.model.SpaceshipVO;

/**
 * The specification of all operations allowed to do over spaceship domain
 * 
 * @author David de Miguel Otero
 */
public interface SpaceshipsService {

	/**
	 * Gets a spaceship by its identifier
	 * @param id The spaceship's identifier
	 * @return The {@link SpaceshipVO} entity whose identifier is the input parameter 
	 */
	SpaceshipVO getSpaceshipById(long id);

	/**
	 * Allows to return a list of spaceships by filter's conditions
	 * @param filter a {@link SpaceshipRequestFilterDTO} contains the data to filter the spaceships
	 * @return a list of spaceships accomplish the filter conditions 
	 */
	List<SpaceshipVO> getAllSpaceships(SpaceshipRequestFilterDTO filter);

	/**
	 * Allows to save a new spaceship 
	 * @param spaceship the future saved {@link SpaceshipVO} entity 
	 * @return a {@link SpaceshipVO} entity with assigned identifier
	 */
	SpaceshipVO createNewSpaceship(SpaceshipVO spaceship);

	/**
	 * Allows to update a {@link SpaceshipVO} vo by its identifier
	 * @param id the identifier of the spaceship will be updated
	 * @param spaceship {@link SpaceshipVO} with information will be updated
	 * @return the {@link SpaceshipVO} with updated information
	 */
	SpaceshipVO modifySpaceShipData(long id, SpaceshipVO spaceship);

	/**
	 * Deletes a {@link SpaceshipVO} by its identifier
	 * @param id the identifier of the spaceship will be deleted
	 */
	void deleteSpaceshipById(long id);

}
