package dts.com.vn.request;

import lombok.Data;

@Data
public class ConditionRequest {

	private Long conditionId;

	private String conditionName;

	private Boolean isConfirm;

	private String messageMt;

	private String messageMt2;

	private Boolean isSoapConfirm;

	private Boolean isChange;

}
