package dts.com.vn.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddServiceInfoRequest {

	private Long serviceInfoId;

	private String infoName;

	private String infoValue;

	private String description;

	private String staDate;

	private String endDate;

	private Long programId;

}
