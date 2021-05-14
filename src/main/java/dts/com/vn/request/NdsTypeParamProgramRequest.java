package dts.com.vn.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NdsTypeParamProgramRequest {

	private Long ndsTypeParamKey;

	private Long servicePackageId;

	private Long serviceProgramId;

	private String ndsType;

	private String ndsParam;

	private String ndsValue;

	private String staDatetime;

	private String endDatetime;
}
