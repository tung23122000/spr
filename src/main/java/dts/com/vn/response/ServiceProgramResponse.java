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

	private String extendEndDate;// Ngày hết hiệu lực

	private String description;// Mô tả chương trình

	private String chargeType; // Loại gói cước

	private String minusMethod; // Tài khoản trừ

	private Long minStepMinus; // số tiền tối thiểu trừ

	private String checkStepType; // check step type

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
		this.extendEndDate = Objects.nonNull(service.getExtendEndDate())
				? DateTimeUtil.formatInstant(service.getExtendEndDate(), "dd/MM/yyyy HH:mm:ss")
				: "";
		this.minStepMinus = service.getMinStepMinus();
		this.checkStepType = service.getCheckStepType();
	}


}
