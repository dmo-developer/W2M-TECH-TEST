package es.dmo.project.w2m_technical_test.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class W2MTechTestAppExceptionHandler {

	@ExceptionHandler({ MethodArgumentTypeMismatchException.class })
	public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(
			final MethodArgumentTypeMismatchException exception) {
		final String block = new StringBuilder("""
				A ver, no todos somos perfectos...aunque podríamos haber mirado el swagger del API, eh???!!!.
				Parece que te has equivocado con el parámetro: """).append(exception.getPropertyName())
				.append(" que debe tener un valor de tipo: ").append(exception.getRequiredType())
				.append(" y tú le has metido el valor: ").append(exception.getValue()).append("""
						. Por eso se ha producido un fallo del tipo:
							""").append(exception.getRootCause()).append("""
						.
						Ánimo y vuélvelo a intentar.""").toString();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(block);
	}

	@ExceptionHandler({ SpaceshipNotFoundException.class })
	public ResponseEntity<Object> handleSpaceshipNotFoundException(final SpaceshipNotFoundException exception) {
		final String message = """
						¡¡¡Pero se puede saber qué haces buscando naves espaciales que no existen.!!!
						Corre y ejecuta getAllSpaceships para saber qué naves hay.
				""";
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
	}

	@ExceptionHandler({ HttpMessageNotReadableException.class })
	public ResponseEntity<Object> handleHttpMessageNotReadableException(
			final HttpMessageNotReadableException exception) {
		final String message = exception.getMessage();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
	}

	@ExceptionHandler({ InvalidFormatException.class })
	public ResponseEntity<Object> handleInvalidFormatException(final HttpMessageNotReadableException exception) {
		final String message = "Me da la impresión de que te has equivocado en el formato de algún parámetro. A ver si este mensaje te da más pistas: "
				+ exception.getMessage();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
	}

	@ExceptionHandler({ NumberFormatException.class })
	public ResponseEntity<Object> handleNumberFormatException(final NumberFormatException exception) {
		final String message = "Revisa este valor porque no es numérico: " + exception.getMessage();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
	}

	@ExceptionHandler({ IllegalArgumentException.class })
	public ResponseEntity<Object> handleIllegalArgumentException(final IllegalArgumentException exception) {
		final String block = """
				   Esto es una excepción personalizada.
				   Parece que algo no ha ido bien por este motivo:
				""".concat(exception.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(block);
	}

}
