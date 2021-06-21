package dts.com.vn.request;

import lombok.Data;

@Data
public class MapCommandAliasRequest {
    private Long mapCommandAliasId;

    private Long servicePackage;

    private Long serviceProgram;

    private String transCode;

    private String commandAlias;

    private Boolean isActive;
}
