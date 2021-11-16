package dts.com.vn.request;

import dts.com.vn.ilink.entities.Condition;
import lombok.Data;

import java.util.List;

@Data
public class ListConditionRequest {

	private String programCode;

	private String transaction;

	private List<Condition> listCondition;

}
