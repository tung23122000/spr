package dts.com.vn.entities;

import dts.com.vn.request.AddMapServicePackageRequest;
import dts.com.vn.util.DateTimeUtil;
import lombok.Data;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Data
@Entity
@Table(name = "map_service_package", schema = "public")
public class MapServicePackage implements Cloneable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "map_id")
	private Long mapId;

	@ManyToOne
	@JoinColumn(name = "ext_system_id")
	private ExternalSystem extSystem;

	@Column(name = "package_id")
	private Long packageId;

	@Column(name = "reg_map_code")
	private String regMapCode;

	@Column(name = "del_map_code")
	private String delMapCode;

	@Column(name = "prom_code")
	private String promCode;

	@Column(name = "prom_days")
	private String promDays;

	@Column(name = "sta_date")
	private Instant staDate;

	@Column(name = "end_date")
	private Instant endDate;

	@Column(name = "mob_type")
	private String mobType;

	@Column(name = "ext_system_id_in")
	private Long extSystemIdIn;

	@Column(name = "chg_map_code")
	private String chgMapCode;

	@Column(name = "order_id")
	private Long orderId;

	@ManyToOne
	@JoinColumn(name = "program_id")
	private ServiceProgram serviceProgram;

	@Column(name = "on_off")
	private String onOff;

	public MapServicePackage() {
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public MapServicePackage(AddMapServicePackageRequest request, ExternalSystem extSystem, ServiceProgram serviceProgram) {
		this.extSystem = extSystem;
		this.serviceProgram = serviceProgram;
		this.packageId = Objects.nonNull(serviceProgram.getServicePackage()) ? serviceProgram.getServicePackage().getPackageId() : null;
		this.staDate = DateTimeUtil.convertStringToInstant(request.getStartDate(), "dd/MM/yyyy HH:mm:ss");
		this.endDate = DateTimeUtil.convertStringToInstant(request.getEndDate(), "dd/MM/yyyy HH:mm:ss");
		this.promCode = request.getPromCode();
		this.mobType = request.getMobType();
		this.promDays = request.getPromDays();
		this.onOff = request.getOnOff();
		this.regMapCode = request.getRegMapCode();
		this.delMapCode = request.getDelMapCode();
		this.chgMapCode = request.getChgMapCode();
	}
}
