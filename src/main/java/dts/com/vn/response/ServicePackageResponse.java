package dts.com.vn.response;

import dts.com.vn.entities.ServicePackage;
import dts.com.vn.util.DateTimeUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class ServicePackageResponse {

	private Long packageId; // Id gói cước

	private String name; // Tên gói cước

	private String code; // Mã gói cước

	private Long serviceTypeId;// Id Loại gói cước

	private String serviceTypeName;// Loại gói cước

	private String mobType;// Loại thuê bao

	private Long listId;// Danh sách thuê bao

	private Long serviceId;// Id Nguồn đăng ký

	private String serviceName;// Nguồn đăng ký

	private String groupCode;// nhóm gói cước

	private String gprsType; // Loại Data (3G/4G)

	private String staDate; // Ngày bắt đầu

	private String endDate; // Ngày kết thúc

	private Long bookMethod;

	private Long minStepMinus;

	private String status;

	private String countryCode; // mã CVQT

	private String delayTimeCVQT; // Thời gian delay với CVQT

	public ServicePackageResponse(ServicePackage service) {
		super();
		this.packageId = service.getPackageId();
		this.code = service.getCode();
		this.name = service.getName();
		this.mobType = service.getMobType();
		this.groupCode = service.getGroupCode();
		this.serviceTypeId =
				Objects.nonNull(service.getServiceType()) ? service.getServiceType().getServiceTypeId()
						: null;
		this.serviceTypeName =
				Objects.nonNull(service.getServiceType()) ? service.getServiceType().getName() : "";
		this.staDate = Objects.nonNull(service.getStaDate())
				? DateTimeUtil.formatInstant(service.getStaDate(), "dd/MM/yyyy HH:mm:ss")
				: "";
		this.endDate = Objects.nonNull(service.getEndDate())
				? DateTimeUtil.formatInstant(service.getEndDate(), "dd/MM/yyyy HH:mm:ss")
				: "";
		this.listId = service.getListId();
		this.gprsType = service.getGprsType();
		this.serviceName =
				Objects.nonNull(service.getServices()) ? service.getServices().getServiceName() : "";
		this.serviceId =
				Objects.nonNull(service.getServices()) ? service.getServices().getServiceId() : null;
		this.status = service.getStatus();
		this.countryCode = service.getCountryCode();
		this.delayTimeCVQT = service.getDelayTimeCVQT();
	}
}
