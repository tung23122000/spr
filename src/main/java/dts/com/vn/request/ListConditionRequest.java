package dts.com.vn.request;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class ListConditionRequest {

	private String programCode;

	private String transaction;

	private List<HashMap<String, String>> listCondition;

}
