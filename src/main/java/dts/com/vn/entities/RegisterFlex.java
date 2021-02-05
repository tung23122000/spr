package dts.com.vn.entities;

import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "register_flex", schema = "public")
public class RegisterFlex {

  @Id
  @Column(name = "register_flex_id")
  private Long registerFlexId;
  
  @Column(name = "isdn")
  private String isdn;

  @Column(name = "reg_datetime")
  private Instant regDatetime;

  @Column(name = "package_id")
  private Long packageId;

  @Column(name = "program_id")
  private Long programId;

  @Column(name = "process_id")
  private Long processId;

  @Column(name = "last_process_id")
  private Long lastProcessId;

  @Column(name = "update_datetime")
  private Instant updateDatetime;

  @Column(name = "volume_next")
  private String volumeNext;

  @Column(name = "vol_units")
  private String volUnits;

  @Column(name = "vol_num")
  private String volNum;

  @Column(name = "volume")
  private String volume;
}
