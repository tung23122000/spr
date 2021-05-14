package dts.com.vn.response;

import dts.com.vn.entities.BucketsInfo;
import dts.com.vn.util.DateTimeUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class BucketsInfoResponse {

	private Long bucketsId;// Id đấu nối

	private Long packageId; // packageId

	private Long programId; // program id

	private String programDescription; // program id

	private String bucType;// mã tài khoản

	private String bucName;// tên tài khoản, tên bundle trên IN

	private String bucAddUnit; // cộng dồn dung lượng

	private String bundleType;// loại bundle

	private String bucAddDay;// Cộng dồn ngày

	private Long subDelayTime;// thời gian delay

	private String startDate;// ngày bắt đầu

	private String endDate;// ngày kết thúc

	private String subServiceName;// tên subscription trên IN

	private String mobType;// loại thê bao

	private String subsType; // loại subscription

	private Long bucUnit; // lưu lượng

	private String accountType; // Loại tài khoản trên IN bundle / sub

	public BucketsInfoResponse(BucketsInfo bucketsInfo) {
		this.bucketsId = bucketsInfo.getBucketsId();
		this.bucType = bucketsInfo.getBucType();
		this.bucName = bucketsInfo.getBucName();
		this.bucAddUnit = bucketsInfo.getBucAddUnit();
		this.bundleType = bucketsInfo.getBundleType();
		this.bucAddDay = bucketsInfo.getBucAddDay();
		this.subDelayTime = bucketsInfo.getSubsDelayTime();
		this.startDate = Objects.nonNull(bucketsInfo.getStaDate())
				? DateTimeUtil.formatInstant(bucketsInfo.getStaDate(), "dd/MM/yyyy HH:mm:ss")
				: "";
		this.endDate = Objects.nonNull(bucketsInfo.getEndDate())
				? DateTimeUtil.formatInstant(bucketsInfo.getEndDate(), "dd/MM/yyyy HH:mm:ss")
				: "";
		this.subServiceName = bucketsInfo.getSubServiceName();
		this.mobType = bucketsInfo.getMobType();
		this.subsType = bucketsInfo.getSubsType();
		this.packageId = bucketsInfo.getPackageId();
		this.programId = Objects.nonNull(bucketsInfo.getServiceProgram())
				? bucketsInfo.getServiceProgram().getProgramId()
				: null;
		this.bucUnit = bucketsInfo.getBucUnit();
		this.programDescription = Objects.nonNull(bucketsInfo.getServiceProgram())
				? bucketsInfo.getServiceProgram().getDescription()
				: "";
		this.accountType = bucketsInfo.getAccountType();
	}

}
