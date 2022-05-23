package dts.com.vn.response;

import dts.com.vn.entities.MapCommandAlias;
import lombok.Data;

import java.util.Objects;

@Data
public class MapCommandAliasResponse {

	private Long cmdAliasId;

	private Long servicePackageId;

	private Long serviceProgramId;

	private String smsMo;

	private String cmdTransCode;

	private String soapRequest;

	private String nameInSelfCare;

	private Boolean isDisplayInSelfCare;

	public MapCommandAliasResponse(MapCommandAlias entity) {
		this.cmdAliasId = entity.getCmdAliasId();
		this.servicePackageId = Objects.nonNull(entity.getServicePackage()) ? entity.getServicePackage().getPackageId() : null;
		this.serviceProgramId = Objects.nonNull(entity.getServiceProgram()) ? entity.getServiceProgram().getProgramId() : null;
		this.smsMo = entity.getSmsMo();
		this.cmdTransCode = entity.getCmdTransCode();
		this.soapRequest = entity.getSoapRequest();
		this.nameInSelfCare = entity.getNameInSelfCare();
		this.isDisplayInSelfCare = entity.getIsDisplayInSelfCare();
	}

}
