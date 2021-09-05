package dts.com.vn.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class RenewData {

  @Id
  @Column(name = "reg_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long regId;
  
  @Column(name = "isdn")
  private String isdn;
  
  @Column(name = "group_code")
  private String groupCode;

  @Column(name = "service_number")
  private String serviceNumber;
  
  @Column(name = "source_code")
  private String sourceCode;

  @Column(name = "command_code")
  private String commandCode;

  @Column(name = "ext_retry_num")
  private Long extRetryNum;
}
