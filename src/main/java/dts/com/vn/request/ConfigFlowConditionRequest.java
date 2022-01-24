package dts.com.vn.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ConfigFlowConditionRequest {

	private Long conditionId;

	private String flowKey;

	private String flowName;

	@JsonProperty
	private boolean isConfig;

}
