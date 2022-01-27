package dts.com.vn.response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportServicePackageResponse {

	private String packageCode;

	private String packageName;

	private String serviceGroup;

	private Integer numberRecordSuccess;

	private Integer numberRecordFailed;

	private List<ListCommandResponse> listCommand;
}