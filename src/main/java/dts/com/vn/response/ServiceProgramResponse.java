package dts.com.vn.response;

import dts.com.vn.entities.ServiceProgram;
import dts.com.vn.util.DateTimeUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class ServiceProgramResponse {

	private Long programId; // id CTKM

	private Long packageId; // id goi cuoc

	private String packageCode; // mã gói cước

	private Long chargePrice; // giá tiền

	private String isMinusIn;// trừ tiền trên IN

	private Long chargeTime;// số ngày hưởng

	private String autoExtend;// Gia hạn

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

	private Boolean isDefaultProgram;

	public ServiceProgramResponse(ServiceProgram service) {
		super();
		this.programId = service.getProgramId();
		this.packageId =
				Objects.nonNull(service.getServicePackage()) ? service.getServicePackage().getPackageId()
						: null;
		this.packageCode =
				Objects.nonNull(service.getServicePackage()) ? service.getServicePackage().getCode() : "";
		this.chargePrice = service.getChargePrice();
		this.isMinusIn = service.getIsNinusIn();
		this.chargeTime = service.getChargeTime();
		this.autoExtend = service.getAutoExtend();
		this.numExtend = service.getNumExtend();
		this.vnptPckCode = service.getVnptPckCode();
		this.description = service.getDescription();
		this.chargeType = service.getChargeType();
		this.minusMethod = service.getMinusMethod();
		this.staDate = Objects.nonNull(service.getStaDate())
				? DateTimeUtil.formatInstant(service.getStaDate(), "dd/MM/yyyy HH:mm:ss")
				: "";
		this.endDate = Objects.nonNull(service.getEndDate())
				? DateTimeUtil.formatInstant(service.getEndDate(), "dd/MM/yyyy HH:mm:ss")
				: "";
		this.minStepMinus = service.getMinStepMinus();
		this.checkStepType = service.getCheckStepType();
		this.programCode = service.getProgramCode();
		if (service.getAllowIsdnStatus().equals("1")){
			this.allowIsdnStatus = true;
		}else {
			this.allowIsdnStatus = false;
		}
		this.ccspServiceCode = service.getCcspServiceCode();
		this.ccspResultCode = service.getCcspResultCode();
		this.number1 = service.getNumber1();
		this.number2 = service.getNumber2();
		this.transCode = service.getTransCode();
		this.totalUnit = service.getTotalUnit();
		this.dateBeforeRenew = service.getDateBeforeRenew();
		this.packageIdNext = service.getPackageIdNext();
		this.programIdNext = service.getProgramIdNext();
		this.msgBeforeRenew = service.getMsgBeforeRenew();
		this.isDefaultProgram = service.getIsDefaultProgram();
	}


}
