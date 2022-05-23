package dts.com.vn.entities;

import lombok.Data;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.Instant;
import java.util.List;

@Data
@Entity
@Table(name = "register", schema = "public")
public class Register {

	@Id
	@Column(name = "reg_id", unique = true, nullable = false)
	private Long regId;

	@Column(name = "service_id")
	private Long serviceId;

	@Column(name = "isdn")
	private String isdn;

	@Column(name = "isdn_send")
	private String isdnSend;

	@Column(name = "mob_type")
	private String mobType;

	@Column(name = "reg_datetime")
	private Instant regDatetime;

	@Column(name = "sta_datetime")
	private Instant staDatetime;

	@Column(name = "end_datetime")
	private Instant endDatetime;

	@Column(name = "expire_datetime")
	private Instant expireDatetime;

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
	private Integer deleteReason;

	@Column(name = "prefix_info_id")
	private Long prefixInfoId;

	@Column(name = "prom_service_id")
	private Long promServiceId;

	@Column(name = "prom_detail_id")
	private Long promDetailId;

	@Column(name = "vip_info_id")
	private Long vipInfoId;

	@Column(name = "vip_detail_id")
	private Long vipDetailId;

	@Column(name = "charge_price")
	private String chargePrice;

	@ManyToOne
	@JoinColumn(name = "package_id")
	private ServicePackage servicePackage;

	public Long getPhoneNumber(EntityManager entityManager,String partition) {

		String queryStr = "SELECT COUNT(DISTINCT isdn) " +
				"FROM" + partition +
				"WHERE ((NOW() BETWEEN sta_datetime AND end_datetime) " +
				"OR (end_datetime IS NULL AND NOW() > sta_datetime ))";
		try {
			Query query = entityManager.createNativeQuery(queryStr);
			long count = 0;
			List<BigInteger> list = query.getResultList();
			for (BigInteger bigInteger : list) {
				if(bigInteger!=null){
					count = bigInteger.longValue();
				}
			}
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
