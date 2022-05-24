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

	// tích có reject qua soap hay không 29/04/2022
	private String isSoapConfirmTransaction;

	private Boolean isChange;

	// tích có được phép huỷ không (Anh Khánh confirm 23-5-2022)
	private Boolean isDelete;

}
