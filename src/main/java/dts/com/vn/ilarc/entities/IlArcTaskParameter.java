package dts.com.vn.ilarc.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "il_arc_task_parameter", schema = "ilarc")
public class IlArcTaskParameter {

	@EmbeddedId
	private EmbeddableWithoutId embeddableWithoutId;

	@Column(name = "parameter_name")
	private String parameterName;

	@Column(name = "parameter_type")
	private Long parametersType;

	@Column(name = "parameter_value")
	private String parametersValue;

	@Column(name = "req_received_time")
	private Timestamp reqReceivedTime;

	@Embeddable
	@Data
	public static class EmbeddableWithoutId implements Serializable {

		@Column(name = "request_id")
		private Long requestId;

		@Column(name = "task_id")
		private Long taskId;
	}
}