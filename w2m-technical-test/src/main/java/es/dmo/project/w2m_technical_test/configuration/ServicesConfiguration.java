package es.dmo.project.w2m_technical_test.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import es.dmo.project.w2m_technical_test.configuration.aspect.SpaceshipAspect;
import es.dmo.project.w2m_technical_test.mappers.SpaceshipsEntityVOMapper;
import es.dmo.project.w2m_technical_test.repository.SpaceshipsRepository;
import es.dmo.project.w2m_technical_test.service.SpaceshipsService;
import es.dmo.project.w2m_technical_test.service.SpaceshipsServiceImpl;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class ServicesConfiguration {

	@Bean
	public SpaceshipsService spaceshipsService(final SpaceshipsEntityVOMapper mapper,
			final SpaceshipsRepository spaceshipsRepository) {
		return new SpaceshipsServiceImpl(mapper, spaceshipsRepository);
	}

	@Bean
	public SpaceshipAspect spaceshipAspect() {
		return new SpaceshipAspect();
	}

}
