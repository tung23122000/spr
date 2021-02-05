package dts.com.vn.response;

import dts.com.vn.entities.ExternalSystem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExternalSystemResponse {

  private Long extSystemId;

  private String code;

  private String name;

  private String status;

  private String systemType;

  private String systemProgram;

  public ExternalSystemResponse(ExternalSystem entity) {
    this.extSystemId = entity.getExtSystemId();
    this.code = entity.getCode();
    this.name = entity.getName();
    this.status = entity.getStatus();
    this.systemType = entity.getSystemType();
    this.systemProgram = entity.getSystemProgram();
  }

}
