package dts.com.vn.entities;

import dts.com.vn.request.AddServiceProgramRequest;
import dts.com.vn.util.DateTimeUtil;
import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Table(name = "service_program", schema = "public")
public class ServiceProgram implements Cloneable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "program_id")
	private Long programId;

	@ManyToOne
	@JoinColumn(name = "package_id")
	private ServicePackage servicePackage;

	@Column(name = "charge_price")
	private Long chargePrice;

	@Column(name = "charge_type")
	private String chargeType;

	@Column(name = "charge_time")
	private Long chargeTime;

	@Column(name = "minus_method")
	private String minusMethod;

	@Column(name = "max_minus")
	private Long maxMinus;

	@Column(name = "end_program")
	private Instant endProgram;

	@Column(name = "sta_date")
	private Instant staDate;

	@Column(name = "end_date")
	private Instant endDate;

	@Column(name = "auto_extend")
	private String autoExtend;

	@Column(name = "num_extend")
	private Long numExtend;

	@Column(name = "exp_sms_code")
	private String expSmsCode;

	@Column(name = "ext_sms_code")
	private String extSmsCode;

	@Column(name = "end_sms_code")
	private String endSmsCode;

	@Column(name = "new_price")
	private String newprice;

	@Column(name = "command_id")
	private Long commandId;

	@Column(name = "end_exp_sms_code")
	private String endExpSmsCode;

	@Column(name = "export_cdr")
	private String exportCdr;

	@Column(name = "exp_sms_code_send")
	private String expSmsCodeSend;

	@Column(name = "package_id_next")
	private Long packageIdNext;

	@Column(name = "program_id_next")
	private Long programIdNext;

	@Column(name = "description")
	private String description;

	@Column(name = "is_minus_in")
	private String isNinusIn;

	@Column(name = "vnpt_pck_code")
	private String vnptPckCode;

	@Column(name = "vnpt_prom_code")
	private String vnptPromCode;

	@Column(name = "extend_end_date")
	private Instant extendEndDate;

	@Column(name = "allow_isdn_status")
	private String allowIsdnStatus;

	@Column(name = "full_ext_sms_code")
	private String fullExtSmsCode;

	@Column(name = "command_del_id")
	private Long commandDelId;

	@Column(name = "command_del_param")
	private String commandDelParam;

	@Column(name = "min_step_minus")
	private Long minStepMinus;

	@Column(name = "check_step_type")
	private String checkStepType;

	// Mã chương trình
	@Column(name = "program_code")
	private String programCode;

	// CCSP
	@Column(name = "ccsp_service_code")
	private String ccspServiceCode;

	@Column(name = "ccsp_result_code")
	private String ccspResultCode;

	@Column(name = "number1")
	private Long number1; // MAX DATE

	@Column(name = "number2")
	private Long number2; // MAX REGISTER ON MAX DATE

	@Column(name = "trans_code")
	private String transCode; // ACTIONCODE_MAPPING

	@Column(name = "total_unit")
	private Integer totalUnit; // Total Unit Flex Flow

	@Column(name = "date_before_renew")
	private Integer dateBeforeRenew; // Số ngày gửi MT trước khi gia hạn || default = 0

	@Column(name = "msg_before_renew")
	private String msgBeforeRenew; // Tin nhắn trước khi gia hạn n ngày

	@Column(name = "is_default_program")
	private Boolean isDefaultProgram;

	public ServiceProgram() {
	}

	public ServiceProgram(AddServiceProgramRequest request, ServicePackage servicePackage) {
		this.servicePackage = servicePackage;
		this.chargePrice = request.getChargePrice();
		this.isNinusIn = request.getIsMinusIn();
		this.chargeTime = request.getChargeTime();
		this.autoExtend = request.getAutoExtend();
		this.numExtend = request.getNumExtend();
		this.vnptPckCode = request.getVnptPckCode();
		this.staDate = DateTimeUtil.convertStringToInstant(request.getStaDate(), "dd/MM/yyyy HH:mm:ss");
		this.extendEndDate =
				DateTimeUtil.convertStringToInstant(request.getExtendEndDate(), "dd/MM/yyyy HH:mm:ss");
		this.endDate =
				DateTimeUtil.convertStringToInstant(request.getExtendEndDate(), "dd/MM/yyyy HH:mm:ss");
		this.description = request.getDescription();
		this.chargeType = request.getChargeType();
		this.minusMethod = request.getMinusMethod();
		this.minStepMinus = request.getMinStepMinus();
		this.checkStepType = request.getCheckStepType();
		this.programCode = request.getProgramCode();
		if (request.getAllowIsdnStatus()) {
			this.allowIsdnStatus = "1";
		} else {
			this.allowIsdnStatus = "0";
		}
		if (request.getCcspServiceCode() != null) {
			this.ccspServiceCode = this.convertToCcspDesign(request.getCcspServiceCode());
		}
		if (request.getCcspResultCode() != null) {
			this.ccspResultCode = this.convertToCcspDesign(request.getCcspResultCode());
		}
		this.number1 = request.getNumber1();
		this.number2 = request.getNumber2();
		this.transCode = request.getTransCode();
		this.totalUnit = request.getTotalUnit();
		this.dateBeforeRenew = request.getDateBeforeRenew();
		this.packageIdNext = request.getPackageIdNext();
		this.programIdNext = request.getProgramIdNext();
		this.msgBeforeRenew = request.getMsgBeforeRenew();
		this.isDefaultProgram = request.getIsDefaultProgram();
	}

	//	Convert A#B#C
	public String convertToCcspDesign(String str) {
		if (str.equals(null)) {
			return null;
		} else {
			String returnStr = "";
			if (str.indexOf(",") > 0) {
				returnStr = str.replaceAll(",", "#").toUpperCase();
			} else if (str.indexOf(" ") > 0) {
				returnStr = str.replaceAll(" ", "#").toUpperCase();
			} else if (str.indexOf("#") > 0) {
				returnStr = str.toUpperCase();
			} else {
				returnStr = str;
			}
			return returnStr;
		}
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
