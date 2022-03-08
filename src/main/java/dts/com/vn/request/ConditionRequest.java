package dts.com.vn.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConditionRequest {

	private Long conditionId;

	private String conditionName;

	private Boolean isConfirm;

	private String messageMt;

	private Boolean isSoapConfirm;

	private Boolean isChange;

	private String language;

}
