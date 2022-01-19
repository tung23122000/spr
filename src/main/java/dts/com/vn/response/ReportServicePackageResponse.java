package dts.com.vn.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Description - Kết quả trả về của package
 *
 * @author - binhDT
 * @created - 17/01/2022
 */
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
