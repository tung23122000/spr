package dts.com.vn.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancelReportResponse implements Comparable<CancelReportResponse> {

	private String phoneNumber;

	private Long quantity;

	private String command;

	private String sourceContent;

	@Override
	public int compareTo(CancelReportResponse o) {
		return 0;
	}
}
