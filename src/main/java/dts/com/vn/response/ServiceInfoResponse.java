package dts.com.vn.response;

import dts.com.vn.entities.ServiceInfo;
import dts.com.vn.util.DateTimeUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class ServiceInfoResponse {

	private Long serviceInfoId;

	private Long packageId;

	private String infoName;

	private String infoValue;

	private String description;

	private String staDate;

	private String endDate;

	private Long programId;

	private String programDescription;

	public ServiceInfoResponse(ServiceInfo serviceInfo) {
		this.serviceInfoId = serviceInfo.getServiceInfoId();
		this.packageId = serviceInfo.getPackageId();
		this.infoName = serviceInfo.getInfoName();
		this.infoValue = serviceInfo.getInfoValue();
		this.description = serviceInfo.getDescription();
		this.staDate = DateTimeUtil.formatInstant(serviceInfo.getStaDate(), "dd/MM/yyyy HH:mm:ss");
		this.endDate = DateTimeUtil.formatInstant(serviceInfo.getEndDate(), "dd/MM/yyyy HH:mm:ss");
		this.programId = Objects.nonNull(serviceInfo.getServiceProgram())
				? serviceInfo.getServiceProgram().getProgramId()
				: null;
		this.programDescription = Objects.nonNull(serviceInfo.getServiceProgram())
				? serviceInfo.getServiceProgram().getDescription()
				: "";
	}


}
