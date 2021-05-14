package dts.com.vn.response;

import dts.com.vn.entities.NdsTypeParamProgram;
import dts.com.vn.util.DateTimeUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class NdsTypeParamProgramResponse {

	private Long ndsTypeParamKey;

	private Long servicePackageId;

	private String servicePackageCode;

	private Long serviceProgramId;

	private String serviceProgramDesc;

	private String ndsType;

	private String ndsParam;

	private String ndsValue;

	private String staDatetime;

	private String endDatetime;

	public NdsTypeParamProgramResponse(NdsTypeParamProgram entity) {
		this.ndsTypeParamKey = entity.getNdsTypeParamKey();
		this.servicePackageId =
				Objects.nonNull(entity.getServicePackage()) ? entity.getServicePackage().getPackageId()
						: null;
		this.servicePackageCode =
				Objects.nonNull(entity.getServicePackage()) ? entity.getServicePackage().getCode() : "";
		this.serviceProgramId =
				Objects.nonNull(entity.getServiceProgram()) ? entity.getServiceProgram().getProgramId()
						: null;
		this.serviceProgramDesc =
				Objects.nonNull(entity.getServiceProgram()) ? entity.getServiceProgram().getDescription()
						: "";
		this.ndsType = entity.getNdsType();
		this.ndsParam = entity.getNdsParam();
		this.ndsValue = entity.getNdsValue();
		this.staDatetime = Objects.nonNull(entity.getStaDatetime())
				? DateTimeUtil.formatInstant(entity.getStaDatetime(), "dd/MM/yyyy HH:mm:ss")
				: "";
		this.endDatetime = Objects.nonNull(entity.getEndDatetime())
				? DateTimeUtil.formatInstant(entity.getEndDatetime(), "dd/MM/yyyy HH:mm:ss")
				: "";
	}


}
