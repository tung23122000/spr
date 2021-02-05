package dts.com.vn.entities;

import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "sms_respond", schema = "public")
public class SmsRespond {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "sms_respond_id")
  private Long smsRespondId;

  @Column(name = "service_id")
  private Long serviceId;

  @Column(name = "sms_type")
  private String smsType;

  @Column(name = "code")
  private String code;

  @Column(name = "contents")
  private String contents;

  @Column(name = "sta_date")
  private Instant staDate;

  @Column(name = "end_date")
  private Instant endDate;

  @Column(name = "sms_param")
  private String smsParam;

  @Column(name = "description")
  private String description;

  @Column(name = "status")
  private String status;

  @Column(name = "service_type_id")
  private String serviceTypeId;

  @Column(name = "language_id")
  private Long languageId;

  @Column(name = "sms_respond_id_pk")
  private Long smsRespondIdPk;
}
