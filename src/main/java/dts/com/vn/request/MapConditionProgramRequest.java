package dts.com.vn.request;

import lombok.Data;

@Data
public class MapConditionProgramRequest {

	private Long id;

	private Long programId;

	private Integer conditionId;

	private Object conditionValue;

}
