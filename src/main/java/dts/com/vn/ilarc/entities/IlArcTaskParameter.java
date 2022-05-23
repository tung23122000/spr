package dts.com.vn.ilarc.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@Table(name = "il_arc_task_parameter", schema = "ilarc")
public class IlArcTaskParameter {

	@EmbeddedId
	private EmbeddableWithoutId embeddableWithoutId;

	@Column(name = "parameter_name")
	private String parameterName;

	@Embeddable
	@Data
	public static class EmbeddableWithoutId implements Serializable {

		@Column(name = "request_id")
		private Long requestId;

		@Column(name = "task_id")
		private Long taskId;

		@Column(name = "parameter_type")
		private Long parametersType;

		@Column(name = "parameter_value")
		private String parametersValue;

	}

}
