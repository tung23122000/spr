package dts.com.vn.ilink.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sas_re_task_parameter", schema = "ilink")
public class SasReTaskParameter {

	@EmbeddedId
	private EmbeddableWithoutId embeddableWithoutId;

	@Column(name = "parameter_Name")
	private String parameterName;

	@Column(name = "parameters_Type")
	private Long parametersType;

	@Column(name = "parameter_value")
	private String parametersValue;

	@Embeddable
	@Data
	public static class EmbeddableWithoutId implements Serializable {

		@Column(name = "request_Id")
		private Long requestId;

		@Column(name = "task_Id")
		private Long taskId;
	}
}
