package dts.com.vn.response;

import dts.com.vn.entities.MapCommandAlias;
import dts.com.vn.util.DateTimeUtil;
import lombok.Data;

import java.util.Objects;

@Data
public class MapCommandAliasResponse {
    private Long mapCommandAliasId;

    private Long servicePackageId;

    private Long serviceProgramId;

    private String transCode;

    private String commandAlias;

    private Boolean isActive;

    public MapCommandAliasResponse(MapCommandAlias entity) {
        this.mapCommandAliasId = entity.getMapCommandAliasId();
        this.servicePackageId =
                Objects.nonNull(entity.getServicePackage()) ? entity.getServicePackage().getPackageId()
                        : null;
        this.serviceProgramId =
                Objects.nonNull(entity.getServiceProgram()) ? entity.getServiceProgram().getProgramId()
                        : null;
        this.transCode = entity.getTransCode();
        this.commandAlias = entity.getCommandAlias();
        this.isActive = entity.getIsActive();
    }
}
