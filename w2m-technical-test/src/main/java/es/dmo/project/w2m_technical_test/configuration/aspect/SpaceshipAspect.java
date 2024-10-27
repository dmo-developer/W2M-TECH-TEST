package es.dmo.project.w2m_technical_test.configuration.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Slf4j
@Configuration
@NoArgsConstructor
public class SpaceshipAspect {

	private final static String MESSAGE = "¡¡¡ATENCIÓN: El id es negativo, amigo!!!.";
	
	@Pointcut(value = "execution ( public * es.dmo.project.w2m_technical_test.service.SpaceshipsServiceImpl.getSpaceshipById(..)) && args(id) ")
	public void pointcutWhenNegativeId(final long id) {
	}

	@Before(value = "pointcutWhenNegativeId(id)")
	public void printLogTraceWhenNegativeId(final long id) {
		if (id < 0) {
			log.info("*****************************************************************");
			log.info("*****************************************************************");
			log.info(MESSAGE);
			log.info("*****************************************************************");
			log.info("*****************************************************************");
		}
	}
}
