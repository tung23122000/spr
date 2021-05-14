package dts.com.vn.response;

import dts.com.vn.entities.MapServicePackage;
import dts.com.vn.util.DateTimeUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class MapServicePackageResponse {

	private Long mapServicePackageId; // id dau coi Billing

	private Long programId; // mã chương trình

	private String programDescription; // mã chương trình

	private String regMapCode; // Mã đăng ký

	private String delMapCode;// Mã hủy

	private String promCode;// Mã khuyến mãi

	private String promDay;// Số ngày khuyến mại

	private String startDate;// Ngày bắt đầu

	private String endDate;// Ngày kết thúc

	private Long extSystemId;// Id Hệ thống nguồn

	private String extSystemCode; // mã hệ thống

	private String extSystemName; // tên hệ thống

	private String mobType;// Loại thuê bao

	private String onOff;// Online/Offline

	private String chgMapCode; // Mã đổi

	public MapServicePackageResponse(MapServicePackage service) {
		this.mapServicePackageId = service.getMapId();
		this.programId =
				Objects.nonNull(service.getServiceProgram()) ? service.getServiceProgram().getProgramId()
						: null;
		this.programDescription =
				Objects.nonNull(service.getServiceProgram()) ? service.getServiceProgram().getDescription()
						: "";
		this.regMapCode = service.getRegMapCode();
		this.delMapCode = service.getDelMapCode();
		this.promCode = service.getPromCode();
		this.promDay = service.getPromDays();
		this.startDate = Objects.nonNull(service.getStaDate())
				? DateTimeUtil.formatInstant(service.getStaDate(), "dd/MM/yyyy HH:mm:ss")
				: "";
		this.endDate = Objects.nonNull(service.getEndDate())
				? DateTimeUtil.formatInstant(service.getEndDate(), "dd/MM/yyyy HH:mm:ss")
				: "";
		this.extSystemId =
				Objects.nonNull(service.getExtSystem()) ? service.getExtSystem().getExtSystemId() : null;
		this.extSystemCode =
				Objects.nonNull(service.getExtSystem()) ? service.getExtSystem().getCode() : null;
		this.extSystemName =
				Objects.nonNull(service.getExtSystem()) ? service.getExtSystem().getName() : null;
		this.mobType = service.getMobType();
		this.onOff = service.getOnOff();
		this.chgMapCode = service.getChgMapCode();
	}


}
