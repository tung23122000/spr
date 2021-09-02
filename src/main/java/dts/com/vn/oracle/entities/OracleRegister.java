package dts.com.vn.oracle.entities;

import dts.com.vn.entities.Register;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "REGISTER")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class OracleRegister implements Serializable {

	@Id
	@Column(name = "REG_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REGISTER_SEQ")
	@SequenceGenerator(name = "REGISTER_SEQ", sequenceName = "register_seq", allocationSize = 1)
	private Long regId;

	@Column(name = "SERVICE_ID")
	private Long serviceId;

	@Column(name = "ISDN")
	private String isdn;

	@Column(name = "ISDN_SEND")
	private String isdnSend;

	@Column(name = "MOB_TYPE")
	private String mobType;

	@Column(name = "REG_DATETIME")
	private Instant regDatetime;

	@Column(name = "STA_DATETIME")
	private Instant staDatetime;

	@Column(name = "END_DATETIME")
	private Instant endDatetime;

	@Column(name = "EXPIRE_DATETIME")
	private Instant expireDatetime;

	@Column(name = "PACKAGE_ID")
	private Long packageId;

	@Column(name = "PROGRAM_ID")
	private Long programId;

	@Column(name = "REG_EXT_SYS_ID")
	private Long regExtSysId;

	@Column(name = "END_EXT_SYS_ID")
	private Long endExtSysId;

	@Column(name = "GROUP_CODE")
	private String groupCode;

	@Column(name = "EXTEND_NUM")
	private Long extendNum;

	@Column(name = "PRE_REG_ID")
	private Long preRegId;

	@Column(name = "PROCESS_ID")
	private Long processId;

	@Column(name = "IMSI")
	private String imsi;

	@Column(name = "PROFILE")
	private String profile;

	@Column(name = "SERVICE_CODE")
	private String serviceCode;

	@Column(name = "GROUP_CHARGE")
	private String groupCharge;

	@Column(name = "SUB_TYPE")
	private String subType;

	@Column(name = "CB_STATUS")
	private String cbStatus;

	@Column(name = "EXTEND_STATUS")
	private String extendStatus;

	@Column(name = "SMS_EXP_STATUS")
	private String smsExpStatus;

	@Column(name = "SMS_END_STATUS")
	private String smsEndStatus;

	@Column(name = "END_PROCESS_ID")
	private Long endProcessId;

	@Column(name = "EXT_RETRY_NUM")
	private Long extRetryNum;

	@Column(name = "CREATE_DATETIME")
	private Instant createDatetime;

	@Column(name = "DELETE_REASON")
	private Integer deleteReason;

	@Column(name = "PREFIX_INFO_ID")
	private Long prefixInfoId;

	@Column(name = "PROM_SERVICE_ID")
	private Long promServiceId;

	@Column(name = "PROM_DETAIL_ID")
	private Long promDetailId;

	@Column(name = "VIP_INFO_ID")
	private Long vipInfoId;

	@Column(name = "VIP_DETAIL_ID")
	private Long vipDetailId;

	@Column(name = "CHARGE_PRICE")
	private String chargePrice;

	public OracleRegister(Register registerDto) {
		this.serviceId = registerDto.getServiceId();
		this.isdn = registerDto.getIsdn();
		this.isdnSend = registerDto.getIsdnSend();
		this.mobType = registerDto.getMobType();
		this.regDatetime = registerDto.getRegDatetime();
		this.staDatetime = registerDto.getStaDatetime();
		this.endDatetime = registerDto.getEndDatetime();
		this.expireDatetime = registerDto.getExpireDatetime();
		this.packageId = registerDto.getServicePackage().getPackageId();
		this.programId = registerDto.getProgramId();
		this.regExtSysId = registerDto.getRegExtSysId();
		this.groupCode = registerDto.getGroupCode();
		this.extendNum = registerDto.getExtendNum();
		this.imsi = registerDto.getImsi();
		this.profile = registerDto.getProfile();
		this.serviceCode = registerDto.getServiceCode();
		this.groupCharge = registerDto.getGroupCharge();
		this.subType = registerDto.getSubType();
		this.cbStatus = registerDto.getCbStatus();
		this.extRetryNum = registerDto.getExtRetryNum();
		this.prefixInfoId = registerDto.getPrefixInfoId();
		this.promServiceId = registerDto.getPromServiceId();
		this.promDetailId = registerDto.getPromDetailId();
		this.vipInfoId = registerDto.getVipInfoId();
		this.vipDetailId = registerDto.getVipDetailId();
		this.chargePrice = registerDto.getChargePrice();
	}

}
