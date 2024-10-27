package es.dmo.project.w2m_technical_test.model;

import java.io.Serial;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SpaceshipRequestFilterDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = 4246637197042560293L;

	private String name;
	@Builder.Default
	private int page = 0;
	@Builder.Default
	private int size = 99;

}
