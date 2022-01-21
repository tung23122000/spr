package dts.com.vn.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "config_flow_condition", schema = "public")
public class ConfigFlowCondition {

	@Id
	@Column(name = "condition_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long conditionId;

	@Column(name = "flow_name")
	private String flowName;

	@Column(name = "is_config", columnDefinition = "boolean default true")
	private boolean isConfig;
}
