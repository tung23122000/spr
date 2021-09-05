package dts.com.vn.entities;

import lombok.Data;

import javax.persistence.*;

@Data
public class RenewData {

  private Long regId;

  private String isdn;

  private String groupCode;

  private String serviceNumber;

  private String sourceCode;

  private String commandCode;

  private Long extRetryNum;

}
