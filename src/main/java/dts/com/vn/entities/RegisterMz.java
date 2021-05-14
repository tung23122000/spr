package dts.com.vn.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Table(name = "register_mz", schema = "public")
public class RegisterMz {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reg_id")
	private Long regId;

	@Column(name = "service_id")
	private Long serviceId;

	@Column(name = "isdn")
	private String isdn;

	@Column(name = "isdn_send")
	private String isdnSend;

	@Column(name = "mob_type")
	private String mobType;

	@Column(name = "reg_datetime", nullable = false)
	private Instant regDatetime;

	@Column(name = "sta_datetime")
	private Instant staDatetime;

	@Column(name = "end_datetime")
	private Instant endDatetime;

	@Column(name = "expire_datetime")
	private Instant expireDatetime;

	@Column(name = "package_id")
	private Long packageId;

	@Column(name = "program_id")
	private Long programId;

	@Column(name = "reg_ext_sys_id")
	private Long regExtSysId;

	@Column(name = "end_ext_sys_id")
	private Long endExtSysId;

	@Column(name = "group_code")
	private String groupCode;

	@Column(name = "extend_num")
	private Long extendNum;

	@Column(name = "pre_reg_id")
	private Long preRegId;

	@Column(name = "process_id")
	private Long processId;

	@Column(name = "imsi")
	private String imsi;

	@Column(name = "profile")
	private String profile;

	@Column(name = "service_code")
	private String serviceCode;

	@Column(name = "group_charge")
	private String groupCharge;

	@Column(name = "sub_type")
	private String subType;

	@Column(name = "cb_status")
	private String cbStatus;

	@Column(name = "extend_status")
	private String extendStatus;

	@Column(name = "sms_exp_status")
	private String smsExpStatus;

	@Column(name = "sms_end_status")
	private String smsEndStatus;

	@Column(name = "end_process_id")
	private Long endProcessId;

	@Column(name = "ext_retry_num")
	private Long extRetryNum;

	@Column(name = "create_datetime")
	private Instant createDatetime;

	@Column(name = "delete_reason")
	private String deleteReason;
}
