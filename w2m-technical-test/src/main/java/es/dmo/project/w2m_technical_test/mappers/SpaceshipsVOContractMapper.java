package es.dmo.project.w2m_technical_test.mappers;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import es.dmo.project.model.SpaceshipContract;
import es.dmo.project.w2m_technical_test.model.SpaceshipVO;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface SpaceshipsVOContractMapper {

	@Mapping(target = "identifier", source = "identifier")
	@Mapping(target = "spaceShipName", source = "spaceShipName")
	SpaceshipContract toSpaceshipContract(SpaceshipVO spaceshipVO);

	@InheritInverseConfiguration
	SpaceshipVO toSpaceshipVO(SpaceshipContract spaceshipContract);

	List<SpaceshipContract> spaceshipVOToSpaceshipContractList(List<SpaceshipVO> SpaceshipVOs);

}
