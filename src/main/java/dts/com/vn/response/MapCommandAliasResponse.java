package dts.com.vn.response;

import dts.com.vn.entities.MapCommandAlias;
import dts.com.vn.util.DateTimeUtil;
import lombok.Data;

import java.util.Objects;

@Data
public class MapCommandAliasResponse {
    private Long cmdAliasId;

    private Long servicePackageId;

    private Long serviceProgramId;

    private String smsMo;

    private String soapRequest;

    public MapCommandAliasResponse(MapCommandAlias entity) {
        this.cmdAliasId = entity.getCmdAliasId();
        this.servicePackageId =
                Objects.nonNull(entity.getServicePackage()) ? entity.getServicePackage().getPackageId()
                        : null;
        this.serviceProgramId =
                Objects.nonNull(entity.getServiceProgram()) ? entity.getServiceProgram().getProgramId()
                        : null;
        this.smsMo = entity.getSmsMo();
        this.soapRequest = entity.getSoapRequest();
    }
}
