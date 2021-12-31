package dts.com.vn.request;

import dts.com.vn.entities.JsonIsdn;
import lombok.Data;

import java.util.List;

@Data
public class PackageListRequest {

	private String fileName; // file import isdn

	private List<JsonIsdn> listIsdn; // list isdn

	private Long packageId;

	private Long programId;

	private String staDate;

	private String endDate;

	private String isdnName;

	private String isdnCvCode;

	private String isdnListType;

	private String isdnDisplay;

}
