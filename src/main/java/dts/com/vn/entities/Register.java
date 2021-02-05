package dts.com.vn.entities;

import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

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
}