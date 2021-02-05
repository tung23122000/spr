package dts.com.vn.entities;

import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import dts.com.vn.request.AddServiceProgramRequest;
import dts.com.vn.util.DateTimeUtil;
import lombok.Data;

@Data
@Entity
@Table(name = "service_program", schema = "public")
public class ServiceProgram {

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

  public ServiceProgram() {}
  
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
  }
}
