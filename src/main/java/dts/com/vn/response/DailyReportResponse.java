package dts.com.vn.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyReportResponse {

	private String groupName;

	private List<Object> listPackage;

}
