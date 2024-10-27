package es.dmo.project.w2m_technical_test.exception;

import java.io.Serial;

public class SpaceshipNotFoundException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = -2845095666372045202L;

	public SpaceshipNotFoundException(String message) {
		super(message);
	}
}
