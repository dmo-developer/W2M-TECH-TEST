package es.dmo.project.w2m_technical_test.mappers;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import es.dmo.project.w2m_technical_test.entity.SpaceshipEntity;
import es.dmo.project.w2m_technical_test.model.SpaceshipVO;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface SpaceshipsEntityVOMapper {

	@Mapping(target = "id", source = "identifier")
	@Mapping(target = "name", source = "spaceShipName")
	SpaceshipEntity toSpaceshipEntity(SpaceshipVO spaceshipVO);

	@InheritInverseConfiguration
	SpaceshipVO toSpaceshipVO(SpaceshipEntity spaceshipEntity);

	List<SpaceshipVO> spaceshipEntitiesToSpaceshipVOList(List<SpaceshipEntity> SpaceshipEntities);

}
