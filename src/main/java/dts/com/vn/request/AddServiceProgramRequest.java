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

	private String extendEndDate;// Ngày hết hiệu lực

	private String description;// Mô tả chương trình

	private String chargeType; // Loại gói cước

	private String minusMethod; // Tài khoản trừ

	private Long minStepMinus; // số tiền tối thiểu trừ

	private String checkStepType; // check step type

	private String programCode; // Mã chương trình

	private String fileName; // file import isdn

//	private List<String> listIsdn; // list isdn


	private List<JsonIsdn> listIsdn; // list isdn

	private Long servicePackageId; // PackageID

	private Boolean allowIsdnStatus; //Thông tin đấu nối

	// CCSP
	private String ccspServiceCode;

	private String ccspResultCode;

	private Long number1; // MAX DATE

	private Long number2; // MAX REGISTER ON MAX DATE

	private String transCode; // ACTIONCODE_MAPPING

	private Integer totalUnit; // Total Unit Flex Flow

	private Integer dateBeforeRenew; // Số ngày gửi MT trước khi gia hạn

	private Long packageIdNext;

	private Long programIdNext;

	private String msgBeforeRenew;
}
