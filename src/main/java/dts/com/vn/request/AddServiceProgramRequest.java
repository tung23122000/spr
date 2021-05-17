package dts.com.vn.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddServiceProgramRequest {

	String isMinusIn;// trừ tiền trên IN (true/false)
	private Long serviceProgramId;// id CTKM
	private String packageCode; //mã gói cước
	private Long chargePrice; // giá tiền
	private Long chargeTime;// số ngày hưởng

	private String autoExtend;// Gia hạn (true/false)

	private Long numExtend;// Số lần gia hạn

	private String vnptPckCode;// Mã KM VNPT

	private String staDate;// Ngày hiệu lực

	private String extendEndDate;// Ngày hết hiệu lực

	private String description;// Mô tả chương trình

	private String chargeType; // Loại gói cước

	private String minusMethod; // Tài khoản trừ

	private Long minStepMinus; // số tiền tối thiểu trừ

	private String checkStepType; // check step type

	private String commandAlias; // phân biệt chương trình nào ứng với câu lệnh nào
}
