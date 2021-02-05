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
@Table(name = "cp_sub_act_status", schema = "public")
public class CpSubActStatus {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "sub_id")
  private Long subId;

  @Column(name = "isdn")
  private String isdn;

  @Column(name = "act_status")
  private String actStatus;

  @Column(name = "update_time")
  private Instant updateTime;
}
