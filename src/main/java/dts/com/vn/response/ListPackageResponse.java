package dts.com.vn.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListPackageResponse {

	private String packageCode;

	private String packageName;

	private Integer numberRecordSuccess;

	private Integer numberRecordFailed;

}
