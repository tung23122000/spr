package dts.com.vn.entities;

import dts.com.vn.enumeration.Constant;
import dts.com.vn.request.AddServicePackageRequest;
import dts.com.vn.util.DateTimeUtil;
import lombok.Data;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

@Data
@Entity
@Table(name = "service_package", schema = "public")
public class ServicePackage {

	@Id
	@Column(name = "package_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long packageId;

	@ManyToOne
	@JoinColumn(name = "service_id")
	private Services services;

	@Column(name = "code")
	private String code;

	@Column(name = "default_code")
	private String defaultCode;

	@Column(name = "name")
	private String name;

	@Column(name = "reg_type")
	private String regType;

	@Column(name = "mob_type")
	private String mobType;

	@Column(name = "group_code")
	private String groupCode;

	@ManyToOne
	@JoinColumn(name = "service_type_id")
	private ServiceType serviceType;

	@Column(name = "sta_date")
	private Instant staDate;

	@Column(name = "end_date")
	private Instant endDate;

	@Column(name = "list_id")
	private Long listId;

	@Column(name = "gprs_type")
	private String gprsType;

	@Column(name = "pck_nds_type")
	private String pckNdsType;

	@Column(name = "ser_pack_code")
	private String serPackCode;

	@Column(name = "num_day")
	private Long numDay;

	@Column(name = "display_status")
	private String displayStatus;

	@Column(name = "hlr_on_off")
	private String hlrOnOff;

	@Column(name = "hlr_code")
	private String hlrCode;

	@Column(name = "bus_group")
	private String busGroup;

	@Column(name = "extend_relative")
	private String extendRelative;

	@Column(name = "status")
	private String status;

	@Column(name = "update_date")
	private Date updateDate;

	@Column(name = "input_key")
	private String inputKey;

	@Column(name = "input_value")
	private String inputValue;

	@Column(name = "exclude_package_list")
	private String excludePackageList;

	@Column(name = "expect_result")
	private String expectResult;

	@Column(name = "country_code")
	private String countryCode; // m?? CVQT

	@Column(name = "delay_time")
	private String delayTimeCVQT; // Th???i gian delay v???i CVQT

	@ManyToOne
	@JoinColumn(name = "flow_group_id")
	private FlowGroup flowGroupId;

	@Column(name = "extend_status")
	private Integer extendStatus; // EXTEND STATUS

	@Column(name = "pcrf_group_id")
	private String pcrfGroup; // Nh??m PCRF (format 1,2,3...)

	@Column(name = "is_retry")
	private String isRetry; // C?? t??? ?????ng gia h???n hay kh??ng? 1: c??, 0: kh??ng

	@Column(name = "system_owner")
	private String systemOwner;

	public ServicePackage(AddServicePackageRequest req, ServiceType serviceType, Services services) {
		this.code = req.getPackageCode();
		this.defaultCode = req.getPackageCode();
		this.name = req.getPackageName();
		this.mobType = req.getMobType();
		this.groupCode = req.getGroupCode();
		this.serviceType = serviceType;
		this.staDate = DateTimeUtil.convertStringToInstant(req.getDateStart(), "dd/MM/yyyy HH:mm:ss");
		this.endDate = DateTimeUtil.convertStringToInstant(req.getDateEnd(), "dd/MM/yyyy HH:mm:ss");
		this.listId = req.getListAccount();
		this.gprsType = req.getCategoryData();
		this.services = services;
		this.status = Constant.ACTIVE;
		this.updateDate = new Date();
		this.inputKey = req.getInputKey();
		this.inputValue = req.getInputValue();
		this.excludePackageList = req.getExcludePackageList();
		this.expectResult = req.getExpectResult();
		this.countryCode = req.getCountryCode();
		this.delayTimeCVQT = req.getDelayTimeCVQT();
		this.flowGroupId = req.getFlowGroupId();
		this.extendStatus = req.getExtendStatus();
		this.pcrfGroup = req.getPcrfGroup();
		this.isRetry = req.getIsRetry();
		this.systemOwner = req.getSystemOwner();
		// add 18/11/2021
		this.displayStatus = "1";
	}

	public ServicePackage() {
	}

}
