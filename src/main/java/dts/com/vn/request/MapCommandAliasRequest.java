package dts.com.vn.request;

import lombok.Data;


@Data
public class MapCommandAliasRequest {
    private Long cmdAliasId;

    private Long servicePackage;

    private Long serviceProgram;

    private String cmdTransCode;

    private String smsMo;

    private String soapRequest;

    private String nameInSelfCare;

    private Boolean isDisplayInSelfCare;
}
