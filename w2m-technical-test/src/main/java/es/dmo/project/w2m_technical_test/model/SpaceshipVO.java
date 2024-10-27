package es.dmo.project.w2m_technical_test.model;

import java.io.Serial;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpaceshipVO implements Serializable {

	@Serial
	private static final long serialVersionUID = -4284346915381810892L;

	private Long identifier;
	private String spaceShipName;

}
