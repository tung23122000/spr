package dts.com.vn.request;

import lombok.Data;

@Data
public class ConfigFlowConditionRequest {

	private Long requestId;

	private String flowName;

	private Integer isConfig;

}
