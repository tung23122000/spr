package dts.com.vn.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddBucketsInfoRequest {

	private Long bucketsId;// Id đấu nối

	private String bucName;// tên tài khoản

	private String bucType;// mã tài khoản

	private String bucAddUnit; // cộng dồn dung lượng

	private String bucAddDay;// Cộng dồn ngày

	private String bundleType;// loại bundle

	private String subsType; // loại subscription

	private String startDate;// ngày bắt đầu

	private String endDate;// ngày kết thúc

	private Long subDelayTime;// thời gian delay

	private Long bucUnit; // Lưu lượng

	private String mobType;// loại thê bao

	private Long packageId; // service package code

	private Long programId; // service program id

	private String accountType; // loại thuê bao

	private String serviceIn; // Tên Service IN
}
