package dts.com.vn.entities;

import dts.com.vn.request.AddBucketsInfoRequest;
import dts.com.vn.util.DateTimeUtil;
import lombok.Data;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Data
@Entity
@Table(name = "buckets_info", schema = "public")
public class BucketsInfo implements Cloneable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "buckets_id", nullable = false)
	private Long bucketsId;

	@Column(name = "package_id", nullable = false)
	private Long packageId;

	@Column(name = "buc_index")
	private String bucIndex;

	@Column(name = "buc_unit")
	private Long bucUnit;

	@Column(name = "rate_plan")
	private String ratePlan;

	@Column(name = "f_negdur")
	private String fNegdur;

	@Column(name = "sta_date")
	private Instant staDate;

	@Column(name = "end_date")
	private Instant endDate;

	@Column(name = "buc_type")
	private String bucType;

	@Column(name = "buc_name")
	private String bucName;

	@Column(name = "buc_add_unit")
	private String bucAddUnit;

	@Column(name = "buc_add_day")
	private String bucAddDay;

	@Column(name = "buc_day")
	private Long bucDay;

	@Column(name = "sub_service_name")
	private String subServiceName;

	@ManyToOne
	@JoinColumn(name = "program_id")
	private ServiceProgram serviceProgram;

	@Column(name = "mob_type")
	private String mobType;

	@Column(name = "subs_type")
	private String subsType;

	@Column(name = "subs_delay_time")
	private Long subsDelayTime;

	@Column(name = "bundle_type")
	private String bundleType;

	@Column(name = "account_type")
	private String accountType;

	@Column(name = "service_in")
	private String serviceIn;


	public BucketsInfo() {
	}

	public BucketsInfo(AddBucketsInfoRequest request, ServiceProgram serviceProgram) {
		this.bucName = request.getBucName();
		this.bucType = request.getBucType();
		this.bucAddUnit = request.getBucAddUnit();
		this.bucAddDay = request.getBucAddDay();
		this.bundleType = request.getBundleType();
		this.subsType = request.getSubsType();
		this.subsDelayTime = request.getSubDelayTime();
		this.bucUnit = request.getBucUnit();
		this.mobType = request.getMobType();
		this.packageId =
				Objects.nonNull(serviceProgram) ? serviceProgram.getServicePackage().getPackageId() : null;
		this.serviceProgram = serviceProgram;
		this.staDate =
				DateTimeUtil.convertStringToInstant(request.getStartDate(), "dd/MM/yyyy HH:mm:ss");
		this.endDate = DateTimeUtil.convertStringToInstant(request.getEndDate(), "dd/MM/yyyy HH:mm:ss");
		this.accountType = request.getAccountType();
		this.serviceIn = request.getServiceIn();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
