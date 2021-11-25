package dts.com.vn.request;

import dts.com.vn.entities.JsonIsdn;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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

	private String endDate;// Ngày hết hiệu lực

	private String description;// Mô tả chương trình

	private String chargeType; // Loại gói cước

	private String minusMethod; // Tài khoản trừ

	private Long minStepMinus; // số tiền tối thiểu trừ

	private String checkStepType; // check step type

	private String programCode; // Mã chương trình

	private Long servicePackageId; // PackageID

	private String allowIsdnStatus; //Thông tin đấu nối

	// CCSP
	private String ccspServiceCode;

	private String ccspResultCode;

	private Long number1; // MAX DATE

	private Long number2; // MAX REGISTER ON MAX DATE

	private Integer totalUnit; // Total Unit Flex Flow

	private String dateBeforeRenew; // Số ngày gửi MT trước khi gia hạn

	private Long packageIdNext;

	private Long programIdNext;

	private String msgBeforeRenew;

	private Boolean isDefaultProgram;

	private Boolean isOnKtPro;

	private String expireByOldPackage;
}
