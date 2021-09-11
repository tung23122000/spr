package dts.com.vn.request;

import dts.com.vn.entities.ServicePackage;
import dts.com.vn.entities.ServiceProgram;
import lombok.Data;


@Data
public class MapCommandAliasRequest {
    private Long cmdAliasId;

    private Long servicePackage;

    private Long serviceProgram;

    private String cmdTransCode;

    private String cmdAliasName;

    private String cmdStatus;
}
