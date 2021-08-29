package dts.com.vn.entities;

import dts.com.vn.request.AddServiceInfoRequest;
import dts.com.vn.util.DateTimeUtil;
import lombok.Data;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Data
@Entity
@Table(name = "service_info", schema = "public")
public class ServiceInfo implements Cloneable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "service_info_id")
	private Long serviceInfoId;

	@Column(name = "package_id")
	private Long packageId;

	@Column(name = "info_name")
	private String infoName;

	@Column(name = "info_value")
	private String infoValue;

	@Column(name = "description")
	private String description;

	@Column(name = "sta_date")
	private Instant staDate;

	@Column(name = "end_date")
	private Instant endDate;

	@ManyToOne
	@JoinColumn(name = "program_id")
	private ServiceProgram serviceProgram;

	@Column(name = "serviceInfoTypeId")
	private Long serviceInfoTypeId;

	public ServiceInfo() {
	}

	public ServiceInfo(AddServiceInfoRequest request, ServiceProgram serviceProgram) {
		this.packageId = Objects.nonNull(serviceProgram.getServicePackage())
				? serviceProgram.getServicePackage().getPackageId()
				: null;
		this.infoName = request.getInfoName();
		this.infoValue = request.getInfoValue();
		this.description = request.getDescription();
		this.staDate = DateTimeUtil.convertStringToInstant(request.getStaDate(), "dd/MM/yyyy HH:mm:ss");
		this.endDate = DateTimeUtil.convertStringToInstant(request.getEndDate(), "dd/MM/yyyy HH:mm:ss");
		this.serviceProgram = serviceProgram;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
