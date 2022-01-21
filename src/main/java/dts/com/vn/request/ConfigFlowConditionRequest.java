package dts.com.vn.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ConfigFlowConditionRequest {

	private Long requestId;

	private String flowName;

	@JsonProperty
	private boolean isConfig;

}
