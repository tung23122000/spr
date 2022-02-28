package dts.com.vn.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MapConditionServicePackageRequest {

	private Long packageId;

	private Long programId;

	private List<ConditionRequest> listCondition;

}
